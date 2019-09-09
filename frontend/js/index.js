$(function()
{
  $(".logo-holder").click(returnToHome);
  setBannerSize($(".banner"));
  attachBannerEventListener();
  $(window).resize(() => {
    determineIfMobile();
    setBannerSize($(".banner"));
  });
  verifySession(function(verified){
    if (verified)
    {
      console.log("User is verified");
      userLoggedInUI();
      fetchBannerData();
      checkAdmin();
    }
    else
    {
      console.log("User is logged out");
      userLoggedOutUI();
    }
  });
  attachNavigationBtnEventListeners();
  $(".sidebar-mobile-icon-holder").click(displaySidebar);
});