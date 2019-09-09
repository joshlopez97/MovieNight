let starParams = {"limit": 10, "offset": 0, "orderby": "name", "direction": "ASC"};
let currentStars = [];
let starCache = {};
let currentStarPage = 1;

function displayStars(stars)
{
  currentStars = stars;
  renderMoviePageContainer();
  let movieList = $(".movie-results");
  movieList.empty();
  let filtersContainer = $(".filters-container");
  if (filtersContainer.length === 0)
  {
    filtersContainer = createStarsFilters();
    filtersContainer.insertBefore(".movie-results");
    attachStarFilterEventListeners();
  }
  setActive(".filters-container");
  selectStarFiltersAccordingToParams();
  filtersContainer.append(createStarPages(stars));
  if (stars.length === 0)
  {
    movieList.append("<div class='results-notice'>No Stars Found</div>")
  }
  console.log(stars);
  console.log(stars.length);
  for (let star of stars)
  {
    let birthYear = star['birthYear'];
    if (birthYear === 0 || birthYear === '0')
      birthYear = "";
    let starElem = $(`
      <div class="movie-holder" id="${star['id']}">
        <div class="movie-holder-content">
          <div class="movie-header-holder">
            <span class="movie-title">${star['name']}</span>
            <span class="movie-year">${birthYear}</span>
          </div>
        </div>
      </div>
    `).css("height", "44px");
    movieList.append(starElem);
  }
  $(".movie-holder").click(starClickHandler);
}

function starClickHandler(e){
  createPopupLoader();
  const id = $(this).attr("id");
  fetchStarDetails(id, createStarPopup);
}

function createStarPopup(starData)
{
  $(".popup").remove();
  console.log(starData);
  if (starData !== null)
  {
    const popup = $(`
    <div class="popup movie" style="display: block;">
      <div class="popup-header">
        <div class="popup-movie-header-content">
          <div class="movie-title-large">${starData['name']}</div>
        </div>
      </div>
      <div class="close-icon-holder">
        <img class="close-icon" src="img/close.png" alt="close">
      </div>
      <div class="popup-movie-details">
        <div class="popup-movie-detail-holder">
          <span class="popup-movie-detail-label">Movies: </span><span class="movie-detail-value"></span>
        </div>
      </div>
    </div>
  `);
    let slot = popup.find(".movie-detail-value");
    for (let m of starData['movies'])
    {
      let mlink = $(`<div class="link" id="${m['movieId']}">${m['title']}</div>`).click(movieClickHandler);
      slot.append(mlink);
    }
    $("body").append(popup);
    popup.fadeIn(200);
    $(".close-icon-holder").click(destroyPopup);
    return popup;
  }
  else
  {
    notice("Unable to retrieve star details.");
    return $("<div></div>");
  }
}

function displayStarSearchBar()
{
  starParams = getFreshStarParams();
  resetPages();
  $(".page-title").text("Advanced Star Search");
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
  displayStarSearchPopup();
  $("#change-params").click(displayStarSearchPopup);
  $("#back-to-basic-search").click(displaySearchBar);
  displayStarsCurrentParams();
}

function displayStarsCurrentParams()
{
  getStarsBy(starParams, displayStars);
}

function displayStarSearchPopup()
{
  let popup = createPopup("Star Search", "Search");
  addField(popup, "name", "text", starParams["name"]);
  addField(popup, "Birth Year", "text", starParams["birthYear"], "birthYear");
  addField(popup, "Movie Title", "text", starParams["director"], "movieTitle");
  addField(popup, "year", "text", starParams["year"]);
  addSubmitAction(popup, (e)=>{
    e.preventDefault();
    destroyPopup();
    let formData = getFormData(popup);
    starParams = formData;

    for (let key in formData)
    {
      if (!!formData[key] && formData[key] !== "")
        starParams[key] = formData[key];
    }
    currentStarPage = 1;
    displayStarsCurrentParams();
    return false;
  });
}

function createStarsFilters()
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
        ["Name A-Z", "Name Z-A", "Youngest to Oldest", "Oldest to Youngest"],
        ["name ASC", "name DESC", "birthYear DESC", "birthYear ASC"]
      )
    ).css("float", "right");
  return filterContainer.append(limit, sortby);
}

function attachStarFilterEventListeners()
{
  $("#limit").change((el) => {
    starParams["limit"] = $(el.target).val();
    displayStarsCurrentParams();
  });
  $("#sortby").change((el) => {
    let values = $(el.target).val().split(" ");
    starParams["orderby"] = values[0];
    starParams["direction"] = values[1];
    displayStarsCurrentParams();
  });
}

function selectStarFiltersAccordingToParams()
{
  $(`select#sortby>option[value='${starParams["orderby"] + " " + starParams["direction"]}']`).prop('selected', true);
  $(`select#limit>option[value=${starParams["limit"]}]`).prop('selected', true);
}

function getFreshStarParams()
{
  return {"limit": 10, "offset": 0, "sortby": "name", "direction": "ASC"};
}

function fetchStarDetails(id, callback)
{
  if (!!starCache && !!starCache[id])
    return callback(starCache[id]);
  else
  {
    makePrivilegedRequest("movies/star/get/" + id, "GET", function(res, status, xhr){
      if (!!res && res['resultCode'] === 212)
      {
        console.log(res);
        let starData = res['stars'];
        starCache[id] = starData;
        return callback(starData);
      }
      else
      {
        console.log(status, res);
        console.log("Unable to get movie details for " + id);
        return callback(null);
      }
    });
  }

}