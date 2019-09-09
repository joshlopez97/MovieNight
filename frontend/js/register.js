function createRegisterWindow()
{
  const popup = createAccountPopup("Register");
  addField(popup, "email", "text");
  addField(popup, "password", "password");
  addField(popup, "Confirm Password", "password", "", "confirmpassword");
  ensurePasswordsMatch(popup, "password", "confirmpassword");
  addCaptcha(popup);
  attachRegisterResponseHandlers(popup);
}

function attachRegisterResponseHandlers(popup)
{
  let handleRegisterResponse = function(res, status, formData){
    console.log(status, res, res.responseJSON);
    clearErrorMsgs();
    if (!!res.responseJSON)
      res = res.responseJSON;
    if (res === undefined)
    {
      spawnErrorMsg(popup, "Something went wrong. Please try again later");
    }
    else if (res['resultCode'] === 110)
    {
      let loginData = {"email": formData["email"], "password": formData["password"]};
      makeRequest(
        "idm/login",
        "POST",
        (res) => {
          if (res['resultCode'] === 120)
          {
            console.log("login success");
            setCookie(formData['email'], res['sessionID']);
            submitForm(popup);
          }
        },
        JSON.stringify(loginData), {'Content-Type': 'application/json'});
    }
    else
    {
      let errMsg = res["message"];
      if (!errMsg || typeof errMsg === 'undefined')
      {
        spawnErrorMsg(popup, "Something went wrong");
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
  addSubmitAction(popup, function loginRequest(e)
  {
    let formData = getFormData(popup);
    e.preventDefault();
    console.log(formData);
    console.log(JSON.stringify(formData));
    if (formData["password"] === formData["confirmpassword"])
    {
      const registerData = {"email": formData["email"], "password": formData["password"]};
      makeRequest("idm/register", "POST", (res, status) => handleRegisterResponse(res, status, formData), JSON.stringify(registerData));
    }
    else
    {
      spawnErrorMsg(popup, "Passwords must match");
    }
    return false;
  });
}