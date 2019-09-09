function checkAdmin()
{
  let parsedCookie = JSON.parse(getCookie());
  if (!!parsedCookie && parsedCookie["email"])
  {
    let email = parsedCookie["email"];
    makePrivilegedRequest("idm/privilege", "POST", function(res, status, xhr){
      if (!!res && res['resultCode'] === 140)
      {
        setCookieAdmin();
        adminUI();
      }
      else
      {
        regularUI();
      }
    }, JSON.stringify({"email": email, "plevel": 3}));
  }
}

function isConfirmedAdmin()
{
  let parsedCookie = JSON.parse(getCookie());
  if (!!parsedCookie && !!parsedCookie["admin"])
  {
    let isAdmin = (parsedCookie["admin"] === true);
    console.log("User is admin");
    if (isAdmin)
      adminUI();
    else
      regularUI();
    return isAdmin;
  }
  return false;
}

function createDeleteMovieBtn(movieId)
{
  let btn = $(`<button id="delete-movie">Delete Movie</button>`);
  btn.click((e) => {
    e.preventDefault();
    makePrivilegedRequest("movies/delete/" + movieId, "DELETE", function(res, status, xhr){
      if (!!res && (res['resultCode'] === 240 || res['resultCode'] === 242))
      {
        notice(res['message']);
        destroyPopup();
        $("#" + movieId).fadeOut();
      }
      else if (!!res && !!res['message'])
      {
        notice(res['message']);
      }
    });
    return false;
  });
  return btn;
}

function adminUI()
{
  if ($("#admin-btn").length === 0)
  {
    let dropdown = $("#acc-dropdown").find(".dropdown-content");
    dropdown.append(`<a id="admin-btn">Admin</a>`);
    $("#admin-btn").click(() => {
      openAdminPage();
    });
  }
}

function regularUI()
{
  $("#admin-btn").remove();
}

function openAdminPage()
{
  $(".page-title").text("Admin");
  let adminPageContainer = renderBlankPageContainer();
  let adminPage = $(".adminpage");
  if (adminPage.length === 0)
  {
    adminPage = $(`
    <div class="adminpage">
      <ul class="admin-tasks-list">
        <li id="add-movie" class="admin-task link">Add Movie</li>
        <li id="add-genre" class="admin-task link">Add Genre</li>
        <li id="add-star" class="admin-task link">Add Star</li>
        <li id="add-star-to-movie" class="admin-task link">Add Star To Movie</li>
      </ul>
    </div>
  `);
    adminPageContainer.append(adminPage);
  }
  condenseActivePageElements([".adminpage"]);
  attachAdminEventListeners();
}

function attachAdminEventListeners()
{
  $("#add-movie").click(addMoviePopup);
  $("#add-genre").click(addGenrePopup);
  $("#add-star").click(addStarPopup);
  $("#add-star-to-movie").click(addStarToMoviePopup);
}

function addStarToMoviePopup()
{
  let popup = createPopup("Add Star To Movie", "submit");
  $(".popup-form").addClass("big-form");
  addField(popup, "Star ID", "text", "", "starid");
  addField(popup, "Movie ID", "text", "", "movieid");
  addSubmitAction(popup, function addGenreReq(e){
    e.preventDefault();
    const formData = getFormData(popup);
    console.log(formData);
    makePrivilegedRequest("movies/star/starsin", "POST", function(res, status, xhr){
      if (!!res && res['resultCode'] === 230)
      {
        notice(res['message']);
        destroyPopup();
      }
      else if (!!res && !!res['message'])
      {
        notice(res['message']);
      }
      else
      {
        notice("Something went wrong. Please try again later");
      }
    }, JSON.stringify(formData));
    return false;
  });
}

function addStarPopup()
{
  let popup = createPopup("Add Star", "submit");
  $(".popup-form").addClass("big-form");
  for (let field of ["name"])
  {
    addField(popup, field, "text");
  }
  addField(popup, "Birth Year", "text", "", "birthYear");
  addSubmitAction(popup, function addGenreReq(e){
    e.preventDefault();
    const formData = getFormData(popup);
    console.log(formData);
    makePrivilegedRequest("movies/star/add", "POST", function(res, status, xhr){
      if (!!res && res['resultCode'] === 220)
      {
        notice(res['message']);
        destroyPopup();
      }
      else if (!!res && !!res['message'])
      {
        notice(res['message']);
      }
      else
      {
        notice("Something went wrong. Please try again later");
      }
    }, JSON.stringify(formData));
    return false;
  });
}

function addGenrePopup()
{
  let popup = createPopup("Add Genre", "submit");
  $(".popup-form").addClass("big-form");
  for (let field of ["name"])
  {
    addField(popup, field, "text");
  }
  addSubmitAction(popup, function addGenreReq(e){
    e.preventDefault();
    const formData = getFormData(popup);
    console.log(formData);
    makePrivilegedRequest("movies/genre/add", "POST", function(res, status, xhr){
      if (!!res && res['resultCode'] === 217)
      {
        notice(res['message']);
        destroyPopup();
      }
      else if (!!res && !!res['message'])
      {
        notice(res['message']);
      }
      else
      {
        notice("Something went wrong. Please try again later");
      }
    }, JSON.stringify(formData));
    return false;
  });
}

function addMoviePopup()
{
  let popup = createPopup("Add Movie", "submit");
  $(".popup-form").addClass("big-form");
  for (let field of ["title", "director", "year", "budget", "overview", "revenue"])
  {
    addField(popup, field, "text");
  }
  addRepeatableField(popup, "Genres", "text", "genre");
  addField(popup, "Poster Path", "text", "", "poster_path");
  addField(popup, "Backdrop Path", "text", "", "backdrop_path");
  addSubmitAction(popup, function addMovieReq(e){
    e.preventDefault();
    const formData = getFormData(popup);
    let gdata = [];
    for (let i = 1; i < 10; i++)
    {
      let gel = $(`input.popup-form-field-input[name="${"genre" + i.toString()}"]`);
      if (gel.length !== 0)
      {
        delete formData["genre" + i.toString()];
        gdata.push({"name": gel.val(), "id": 1})
      }
    }
    formData["genres"] = gdata;
    console.log(formData);
    makePrivilegedRequest("movies/add", "POST", function(res, status, xhr){
      if (!!res && res['resultCode'] === 214)
      {
        notice(res['message']);
        destroyPopup();
      }
      else if (!!res && !!res['message'])
      {
        notice(res['message']);
      }
      else
      {
        notice("Something went wrong. Please try again later");
      }
    }, JSON.stringify(formData));
    return false;
  });
}