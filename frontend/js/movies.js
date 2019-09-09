let currentParams = {"limit": 10, "offset": 0, "orderby": "rating", "direction": "DESC"};
let advancedParams = {"title": "", "genre": "", "year": "", "director": ""};
let currentMovies = [];
let movieCache = {};

function getImageLink(path)
{
  return "https://image.tmdb.org/t/p/original" + path;
}

function selectFiltersAccordingToParams()
{
  $(`select#sortby>option[value='${currentParams["orderby"] + " " + currentParams["direction"]}']`).prop('selected', true);
  $(`select#limit>option[value=${currentParams["limit"]}]`).prop('selected', true);
}

function constructMoviesPage()
{
  renderMoviePageContainer();
  condenseActivePageElements([]);
  $(".movie-results").empty();
}

function displayMovies(movies)
{
  currentMovies = movies;
  renderMoviePageContainer();
  let movieList = $(".movie-results");
  movieList.empty();
  let filtersContainer = $(".filters-container");
  if (filtersContainer.length === 0)
  {
    filtersContainer = createFilters();
    filtersContainer.insertBefore(".movie-results");
    attachFilterEventListeners();
  }
  setActive(".filters-container");
  selectFiltersAccordingToParams();
  filtersContainer.append(createPages(movies));
  if (movies.length === 0)
  {
    movieList.append("<div class='results-notice'>No Movies Found</div>")
  }
  for (let movie of movies)
  {
    let poster = "img/default-movie-icon.png";
    if (!!movie["poster_path"])
      poster = getImageLink(movie["poster_path"]);
    let movieElement = $(`
      <div class="movie-holder" id="${movie['movieId']}">
        <div class="movie-thumbnail-holder">
          <img id="mt-${movie["movieId"]}" class="movie-thumbnail" src="${poster}" alt="">
        </div>
        <div class="movie-holder-content">
          <div class="movie-header-holder">
            <span class="movie-title">${movie['title']}</span>
            <span class="movie-year">${movie['year']}</span>
          </div>
          ${createRatingStars(movie['rating'], movie['numVotes']).prop('outerHTML')}
          <div class="movie-details">
            <div class="movie-detail-holder">Director: ${movie['director']}</div>
          </div>
        </div>
      </div>
    `);
    movieList.append(movieElement);
  }
  $(".movie-holder").click(movieClickHandler);
}

function setPoster(movieData)
{
  if (!!movieData && !!movieData["poster_path"])
  {
    $("#mt-" + movieData["movieId"]).attr("src", getImageLink(movieData["poster_path"]));
  }
}

function displayNoMovies()
{
  renderLoadingPageContainer();
  adjustPages();
  constructMoviesPage();
  $(".results-notice").css("display", "none");
}

function displayMoviesCurrentParams(callback=()=>{})
{
  renderLoadingPageContainer();
  adjustPages();
  getMoviesBy(currentParams, function(movies)
  {
    displayMovies(movies);
    callback();
  });
}

function getFreshParams()
{
  let limit = !!currentParams["limit"] ? currentParams["limit"] : 10;
  let direction = !!currentParams["direction"] ? currentParams["direction"] : "DESC";
  let orderby = !!currentParams["orderby"] ? currentParams["orderby"] : "rating";
  return {
    "orderby": orderby,
    "direction": direction,
    "limit": limit,
    "offset": 0,
    "detailed": true
  };
}

function displayMoviesByFirstLetter(letter)
{
  resetPages();
  $(".page-title").text("Browse By Title");
  $(".page-container").append(createTitleLetterSidebar());
  condenseActivePageElements([".title-sidebar-holder", ".filters-container"]);
  $(".genre-dropdown").remove();
  $(".searchbar-holder").remove();
  let titleMatcher = "^" + letter + ".*";
  if (letter === '#')
    titleMatcher = "^[^a-zA-Z].*";
  currentParams = getFreshParams();
  currentParams["title"] = titleMatcher;
  displayMoviesCurrentParams(() => {
    setActiveLetter();
  });
}

function displaySearchBar()
{
  resetPages();
  displayNoMovies();
  $(".page-title").text("Search By Title");
  let searchForm = $(".searchbar-holder");
  if (searchForm.length === 0)
  {
    searchForm = $(`
      <form class="searchbar-holder">
        <input type='text' class='searchbar'>
        <button type="submit" class="searchbar btn"><div class="searchbar-btn-icon"></div></button>
        <div class="link under-search" id="adv-search-btn">Advanced Search</div>
        <div class="link under-search" id="star-search-btn">Star Search</div>
      </form>
    `);
    searchForm.insertAfter(".page-title");
  }
  condenseActivePageElements([".searchbar-holder", ".filters-container"]);
  $("#adv-search-btn").click(displayAdvancedSearchBar);
  $("#star-search-btn").click(displayStarSearchBar)
  $("input[type='text'].searchbar").focus();
  searchForm.submit(()=>
  {
    currentParams = getFreshParams();
    currentParams["title"] = $("input[type='text'].searchbar").val();
    displayMoviesCurrentParams(()=>{
      $(".filters-container").css({
        "margin-top": "30px"
      })
    });
    return false;
  });
}

function displayAdvancedSearchBar()
{
  resetPages();
  $(".page-title").text("Advanced Search");
  if ($(".adv-search-btn-holder").length === 0)
  {
    $(`
      <div class="adv-search-btn-holder">
        <div id="change-params" class="adv-search-btn link">Change Parameters</div>
        <div id="back-to-basic-search" class="adv-search-btn link">Basic Search</div>
      </div>
    `).insertAfter(".page-title");
  }
  condenseActivePageElements([".adv-search-btn-holder", ".filters-container"]);
  displayAdvancedSearchPopup();
  $("#change-params").click(displayAdvancedSearchPopup);
  $("#back-to-basic-search").click(displaySearchBar);
  displayMoviesCurrentParams();
}

function displayAdvancedSearchPopup()
{
  let popup = createPopup("Advanced Search", "Search");
  addField(popup, "title", "text", advancedParams["title"]);
  addField(popup, "genre", "text", advancedParams["genre"]);
  addField(popup, "director", "text", advancedParams["director"]);
  addField(popup, "year", "text", advancedParams["year"]);
  addSubmitAction(popup, (e)=>{
    e.preventDefault();
    destroyPopup();
    let formData = getFormData(popup);
    advancedParams = formData;
    currentParams = getFreshParams();
    for (let key in formData)
    {
      if (!!formData[key] && formData[key] !== "")
        currentParams[key] = formData[key];
    }
    displayMoviesCurrentParams();
    return false;
  });
}

function displayMoviesByGenre(genre)
{
  resetPages();
  $(".page-title").text("Browse By Genre");
  if ($("#genre").length === 0)
  {
    $("<div class='genre-dropdown temp'></div>").append(createSelect("genre", ["Loading..."])).insertAfter(".page-title");
  }
  condenseActivePageElements([".genre-dropdown", ".filters-container"]);
  createGenreDropDown(genre);
  currentParams = getFreshParams();
  currentParams["genre"] = genre;
  displayMoviesCurrentParams();
}

function createGenreDropDown(genre)
{
  if ($("#genre").length === 0 || $(".genre-dropdown.temp").length !== 0)
  {
    getAllGenres(function addGenres(genres)
    {
      let genreNames = [];
      for (let g of genres)
        genreNames.push(g["name"]);
      let genreDropDown = $("<div class='genre-dropdown visible'></div>").append(createSelect("genre", genreNames));
      genreDropDown.change((e) => {
        displayMoviesByGenre($(e.target).val());
      });
      $(".genre-dropdown.temp").remove();
      genreDropDown.insertAfter(".page-title");
      $(`#genre>option:eq(${genre})`).prop('selected', true);
      $("#genre").val(genre);
    });
  }
  else
  {
    $(`#genre>option:eq(${genre})`).prop('selected', true);
    $("#genre").val(genre);
  }
}

function attachFilterEventListeners()
{
  $("#limit").change((el) => {
    currentParams["limit"] = $(el.target).val();
    displayMoviesCurrentParams();
  });
  $("#sortby").change((el) => {
    let values = $(el.target).val().split(" ");
    currentParams["orderby"] = values[0];
    currentParams["direction"] = values[1];
    displayMoviesCurrentParams();
  });
}

function createTitleLetterSidebar()
{
  if ($(".title-sidebar-holder").length === 0)
  {
    let chars = "#abcdefghijklmnopqrstuvwxyz".split('');
    let sidebar = $("<ul class='title-sidebar'></ul>");
    for (let char of chars)
    {
      let id = char === '#' ? 'symb' : char;
      let letterBtn = $(`<li id="${id}-btn" class="title-sidebar-item">
                         ${char}
                       </li>`);
      letterBtn.click(() => displayMoviesByFirstLetter(char));
      sidebar.append(letterBtn)
    }
    return $("<div class='title-sidebar-holder'></div>").append(sidebar);
  }
}

function setActiveLetter()
{
  $(".title-sidebar-item").removeClass("active-letter");
  let activeLetter;
  if (currentMovies.length > 0)
    activeLetter = currentMovies[0]['title'].charAt(0).toLowerCase();
  else
    activeLetter = 'symb';
  if (activeLetter.match(/[^a-zA-Z]/g))
    activeLetter = 'symb';
  $(`#${activeLetter}-btn`).addClass("active-letter");
}

function createFilters()
{
  let filterContainer = $(`<div class="filters-container"></div>`);
  let limit = $(`
      <div class="filter-holder">
        <span class="filter-label">Limit</span>
      </div>`
  )
  .append(createSelect("limit", [10, 25, 50, 100]));
  let sortby = $(`
      <div class="filter-holder">
        <span class="filter-label">Sort By</span>
      </div>`
  )
  .append(
    createSelect(
      "sortby",
      ["Title A-Z", "Title Z-A", "Rating High to Low", "Rating Low to High"],
      ["title ASC", "title DESC", "rating DESC", "rating ASC"]
    )
  ).css("float", "right");
  return filterContainer.append(limit, sortby);
}

function createSelect(id, labels, values=labels)
{
  let select = $(`<select id='${id}' name='${name}' class='filter'></select>`);
  for (let i = 0; i < labels.length; i++)
  {
    select.append(`<option value="${values[i]}">${labels[i]}</option>`);
  }
  return select;
}

