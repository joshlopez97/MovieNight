function attachNavigationBtnEventListeners()
{
  $("#login-btn").click(displayLoginWindow);
  $("#register-btn").click(createRegisterWindow);
  $("#title-btn").click(() => displayMoviesByFirstLetter("#"));
  $("#genre-btn").click(() => displayMoviesByGenre("Action"));
  $("#search-btn").click(displaySearchBar);
  $("#view-cart-btn").click(displayShoppingCart);
  $("#order-history-btn").click(displayOrderHistory);
  $("#logout-btn").click(logout);
}

function attachMobileNavigationBtnEventListeners()
{
  $(".sidebar #login-btn").click(displayLoginWindow);
  $(".sidebar #register-btn").click(createRegisterWindow);
  $(".sidebar #title-btn").click(() => displayMoviesByFirstLetter("#"));
  $(".sidebar #genre-btn").click(() => displayMoviesByGenre("Action"));
  $(".sidebar #search-btn").click(displaySearchBar);
  $(".sidebar #view-cart-btn").click(displayShoppingCart);
  $(".sidebar #order-history-btn").click(displayOrderHistory);
}

function renderMoviePageContainer()
{
  hideSidebar();
  pauseBanner();
  removeLoader();
  return $(".page-container").fadeIn();
}

function renderLoadingPageContainer()
{
  hideSidebar();
  pauseBanner();
  let pc = $(".page-container");
  if (!loaderIsPresent(pc))
    pc.append(getLoader());
  $(".movie-results").empty();
  return pc.fadeIn();
}

function returnToHome(quickly=false)
{
  if (!quickly)
    $(".page-container").fadeOut();
  else
    $(".page-container").css("display", "none");
  hideSidebar();
  removeLoader();
  resumeBanner();
  destroyPopup();
}

function renderBlankPageContainer()
{
  hideSidebar();
  pauseBanner();
  removeLoader();
  $(".movie-results").empty();
  condenseActivePageElements([]);
  return $(".page-container").fadeIn();
}

function condenseActivePageElements(selectors)
{
  $(".visible").removeClass("visible");
  for (let selector of selectors)
    $(selector).addClass("visible");
}

function setActive(selector)
{
  $(selector).addClass("visible");
}