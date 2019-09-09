
let authCookie = document.cookie;

function getCookie()
{
  let cookieKey = "authCookie";
  // return JSON.stringify(
  //   {
  //     "sessionID":"3b5459480222c3fbf3e80128e3efdb869325252271ab5b92db2ee1451426926ef6b129c6df5e0292364477579253294854304a49f9ebbb053e9203cf5f9ec8bc",
  //     "email":"joshlopez97@gmail.com",
  //     "admin": true
  //   }
  // );
  let parsedCookie = RegExp("" + cookieKey + "[^;]+").exec(document.cookie);
  let decoded = decodeURIComponent(!!parsedCookie ? parsedCookie.toString().replace(/^[^=]+./,"") : "{}");
  console.log("Returning Decoded Cookie: " + decoded);
  return decoded;
}

function setCookieAdmin()
{
  let cookieData = JSON.parse(getCookie());
  cookieData["admin"] = true;
  authCookie = document.cookie = `authCookie=${JSON.stringify(cookieData)}`;
}

function setCookie(email, sessionID)
{
  authCookie = document.cookie = `authCookie=${JSON.stringify({"sessionID": sessionID, "email": email, "admin": false})}`;
}

function clearCookie()
{
  authCookie = document.cookie =  "authCookie= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
}


function displayLoginWindow()
{
  const popup = createAccountPopup("Login");
  popup.addClass("login-popup");
  addField(popup, "email", "text");
  addField(popup, "password", "password");
  attachLoginResponseHandlers(popup);
}

function attachLoginResponseHandlers(popup)
{
  let handleLoginResponse = function(res, status, formData){
    if (res === undefined)
    {
      spawnErrorMsg(popup, "Something went wrong. Please try again later");
    }
    else if (res['resultCode'] === 120)
    {
      console.log("login success");
      setCookie(formData['email'], res['sessionID']);
      submitForm(popup);
    }
    else
    {
      clearErrorMsgs(popup);
      let errMsg = res["message"];
      if (errMsg === undefined)
      {
        spawnErrorMsg(popup, "Something went wrong. Please try again later");
      }
      else if (errMsg.toLowerCase().includes("email"))
      {
        spawnErrorMsg(popup, errMsg, "email");
      }
      else if (errMsg.toLowerCase().includes("password"))
      {
        spawnErrorMsg(popup, errMsg, "password");
      }
      else
      {
        spawnErrorMsg(popup, errMsg);
      }
    }
  };
  addSubmitAction(popup, function loginRequest(){
    const formData = getFormData(popup);
    console.log(formData);
    makeRequest(
      "idm/login",
      "POST",
      (res, status) => handleLoginResponse(res, status, formData),
      JSON.stringify(formData), {'Content-Type': 'application/json'});
    return false;
  });
}


function userLoggedInUI()
{
  $("#login-btn").addClass("hidden");
  $("#register-btn").addClass("hidden");
  $("#acc-dropdown").removeClass("hidden");
  $("#view-cart-btn").removeClass("hidden");
  $("#order-history-btn").removeClass("hidden");
  setTimeout(() => {
    $(".login-popup").remove()
  }, 1000);
}

function userLoggedOutUI()
{
  $("#login-btn").removeClass("hidden");
  $("#register-btn").removeClass("hidden");
  $("#acc-dropdown").addClass("hidden");
  $("#view-cart-btn").addClass("hidden");
  $("#order-history-btn").addClass("hidden");
}

function verifySession(callback)
{
  let cookieData = getCookie();
  let parsedCookieData = JSON.parse(cookieData);
  let email = parsedCookieData["email"];
  if (!!cookieData && !!parsedCookieData && !!parsedCookieData["email"] && !!parsedCookieData["sessionID"])
  {
    userLoggedInUI(); // Assume used is logged in for faster UI change, validate session in background
    makeRequest("idm/session", "POST", function(res, status, xhr)
    {
      if (!!res && res['resultCode'] === 130)
      {
        setCookie(email, res['sessionID']);
        return callback(true);
      }
      else if (!!res && res['resultCode'] === 133 || res['resultCode'] === 131)
      {
        console.log(res['message']);
        displayLoginWindow();
        return callback(false);
      }
      else
      {
        console.log(status, res);
        return callback(false);
      }
    }, JSON.stringify({"email": email, "sessionID": parsedCookieData["sessionID"]}), {'Content-Type': 'application/json'});
  }
  else
  {
    console.log("No login data found in cookie " + cookieData);
    return callback(false);
  }
}

function logout()
{
  clearCookie();
  location.reload();
}

function mustBeLoggedInNotice()
{
  destroyPopup();
  returnToHome(true);
  notice("You need to be logged in to do that.");
  displayLoginWindow();
}