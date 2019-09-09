function createFormattedMovieDetails(movieData)
{
  let detailsHolder = $(`<div class="popup-movie-details"></div>`);
  for (let label of ['Year', 'Director', 'Overview', 'Budget', 'Revenue'])
  {
    let key = label.toLowerCase();
    if (!!movieData[key] && movieData[key] !== "")
    {
      detailsHolder.append(`
        <div class="popup-movie-detail-holder">
          <span class="popup-movie-detail-label">${label}: </span><span class="movie-detail-value">${movieData[key]}</span>
        </div>
    `);
    }
  }
  let stars = [];
  for (let star of movieData['stars'])
    stars.push(star['name']);
  detailsHolder.append(`
    <div class="popup-movie-detail-holder">
      <span class="popup-movie-detail-label">Starring: </span><span class="movie-detail-value">${stars.join(', ')}</span>
    </div>
  `);
  return createMovieGenreLinks(movieData) + detailsHolder.prop('outerHTML');
}

function createMovieGenreLinks(movieData)
{
  let genreHolder = $(`<div class="genre-holder"></div>`);
  for (let genre of movieData['genres'])
  {
    let genreLink = $(`<button class='genre-link'>${genre['name']}</button>`);
    genreHolder.append(genreLink);
  }
  return genreHolder.prop('outerHTML');
}

function movieClickHandler(e){
  createPopupLoader();
  const id = $(this).attr("id");
  fetchMovieDetails(id, createMoviePopup);
}

function fetchMovieDetails(id, callback)
{
  if (!!movieCache && !!movieCache[id])
    return callback(movieCache[id]);
  else
  {
    makePrivilegedRequest("movies/get/" + id, "GET", function(res, status, xhr){
      if (!!res && res['resultCode'] === 210)
      {
        let movieData = res['movie'];
        movieCache[id] = movieData;
        return callback(movieData);
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

function createMoviePopup(movieData)
{
  $(".popup").remove();
  console.log(movieData);
  if (movieData !== null)
  {
    let stars = createRatingStars(movieData['rating'], movieData['numVotes'], 120).prop('outerHTML');
    let movieDetails = createFormattedMovieDetails(movieData);
    let poster_path = "img/default-movie-icon.png";
    if (!!movieData["poster_path"])
      poster_path = "https://image.tmdb.org/t/p/w500" + movieData["poster_path"];
    const popup = $(`
    <div class="popup movie" style="display: block;">
      <div class="popup-header">
        <div class="movie-thumbnail-holder">
          <img id="mt-${movieData["movieId"]}" class="movie-thumbnail" src=${poster_path} alt="">
        </div>
        <div class="popup-movie-header-content">
          <div class="movie-title-large">${movieData['title']}</div>
          ${stars}
          <div class="create-rating link"><a>Rate this movie</a></div>
        </div>
      </div>
      <div class="close-icon-holder">
        <img class="close-icon" src="img/close.png" alt="close">
      </div>
      ${movieDetails}
      <form class="popup-form">
        <div class="popup-fields-movies">
          <div class="popup-form-field">
            <div class="popup-form-field-label">Quantity</div>
            <input class="popup-form-field-input" type="number" name="quantity" value="1">
          </div>
        </div>
        <button id="add-to-cart" class="popup-submit">Add to Cart</button>
      </form>
    </div>
  `);
    if (isConfirmedAdmin())
    {
      popup.find(".popup-form").append(createDeleteMovieBtn(movieData["movieId"]));
    }
    $("body").append(popup);
    setPoster(movieData);
    $(".create-rating").click(() => createRatingPrompt(movieData["movieId"]));
    $(".genre-link").click((e) => {
      destroyPopup();
      displayMoviesByGenre($(e.target).text())
    });
    $("#add-to-cart").click((e) => {
      e.preventDefault();
      let formData = getFormData(popup);
      addMovieToCart(movieData["movieId"], movieData["title"], formData["quantity"], ()=>{
        destroyPopup();
      });
      return false;
    });
    popup.fadeIn(200);
    $(".close-icon-holder").click(destroyPopup);
    return popup;
  }
  else
  {
    notice("Unable to retrieve movie details.");
    return $("<div></div>");
  }
}