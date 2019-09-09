let isMobile = false;

function determineIfMobile()
{
  isMobile = $(window).height() / $(window).width() > 1;
  return isMobile;
}

/* Sidebar display for mobile devices */
function displaySidebar()
{
  destroyPopup();
  const sidebar = $(".sidebar");
  const ANIMATE_TIME = 300;
  if (sidebar.length > 0)
  {
    return false;
  }
  else
  {
    $(".icon").addClass("close");
    const buttons = [$("#view-cart-btn"), $("#login-btn"), $("#register-btn"), $("#title-btn"), $("#genre-btn"), $("#search-btn"), $("#order-history-btn")];
    let contents = "<ul class='side sidenav-list'>";
    for (let button of buttons)
    {
      button = $(button);
      if (!button.hasClass("hidden"))
        contents += `<li class='side sidenav-holder'><button id="${button.attr("id")}" class="topbtn">${button.text()}</button></li>`;
    }
    contents += "</ul>";
    $("body").append("<div class='side sidebar'>" + contents + "</div>");
    $(".sidebar #search-btn").text("Search");
    attachMobileNavigationBtnEventListeners();


    sidebar.find("button").attr("class", "navbtn side");
    $(".sidebar").animate({left: "0px"},
      ANIMATE_TIME,
      function () {
        const sidebar_btn = $(".sidebar-mobile-icon-holder");
        sidebar_btn.unbind('click');
        sidebar_btn.click(hideSidebar);
      });
  }
}

function hideSidebar() {
  const sidebar = $(".sidebar");
  if (sidebar.length === 0) {
    return false;
  }
  else {
    $(".icon").removeClass("close");
    $(".sidebar").animate({left: "-325px"},
      300,
      function () {
        sidebar.remove();
        const sidebar_btn = $(".sidebar-mobile-icon-holder");
        sidebar_btn.unbind('click');
        sidebar_btn.click(displaySidebar);
      });
  }
}