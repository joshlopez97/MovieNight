const GATEWAY_URI = "https://movienight.dev/api/g/";

function makeRequest(url, request_type, callback, data=null, headers={})
{
  if (request_type.toLowerCase() === "post")
    headers["Content-Type"] = "application/json";
  headers["Access-Control-Expose-Headers"] = "*";
  console.log(
    "Sending " + request_type +
    " request to " + GATEWAY_URI + url +
    " with data " + data +
    " and headers " + JSON.stringify(headers));
  $.ajax({
    type: request_type,
    url: GATEWAY_URI + url,
    dataType: 'json',
    //json object to sent to the authentication url
    data: data,
    headers: headers,
    success: function (res, status, xhr) {
      let tid = xhr.getResponseHeader("transactionID");
      report(tid, callback);
    },
    error: function (res, status, xhr) {
      console.log("Unable to make request to gateway.", url, request_type, data);
      callback(res, status, xhr);
    }
  });
}

function makePrivilegedRequest(url, request_type, callback, data=null, headers={})
{
  if (request_type.toLowerCase() === "post")
    headers["Content-Type"] = "application/json";
  headers["Access-Control-Expose-Headers"] = "*";
  let cookie = JSON.parse(getCookie());
  let auth_headers = {"email": cookie["email"], "sessionID": cookie["sessionID"]};
  console.log(GATEWAY_URI + url);

  // Check if user is signed in
  // Revision: movie endpoints do not need to be protected
  if (!!auth_headers && !!auth_headers["email"])
  {
    let email = auth_headers["email"];
    Object.assign(headers, auth_headers);
    console.log(
      "Sending " + request_type +
      " request to " + GATEWAY_URI + url +
      " with data " + data +
      " and headers " + JSON.stringify(headers));
    $.ajax({
      type: request_type,
      url: GATEWAY_URI + url,
      dataType: 'json',
      //json object to sent to the authentication url
      data: data,
      headers: headers,
      success: function (res, status, xhr) {
        if (status === 'nocontent')
        {
          let tid = xhr.getResponseHeader("transactionID");
          setCookie(email, xhr.getResponseHeader("sessionid"));
          report(tid, callback, auth_headers);
        }
        else
        {
          console.log("Unable to make request to gateway", res, status, xhr);
          callback(res, status, xhr);
        }
      },
      error: function (res, status, xhr) {
        console.log("Unable to make request to gateway.", url, request_type, data);
        callback(res, status, xhr);
      }
    });
  }
  else
  {
    mustBeLoggedInNotice();
  }
}

function report(tid, callback, headers={})
{
  Object.assign(headers, {'transactionID' : tid});
  headers["Access-Control-Expose-Headers"] = "*";
  $.ajax({
    type: "GET",
    url: GATEWAY_URI + "report",
    headers: headers,
    success: function (res, status, xhr) {
      if (status === 'nocontent')
      {
        let delay = xhr.getResponseHeader("delay");
        setTimeout(() => report(tid, callback), delay);
      }
      else
      {
        callback(res, status, xhr);
      }
    },
    error: function (res, status, xhr) {
      console.log('Report returned error');
      console.log(res.responseJSON);
      callback(res, status, xhr);
    },
  });
}

function getTotalMovieCount(callback)
{
  makePrivilegedRequest(`movies/count?` + $.param(currentParams), "GET", function(res, status, xhr){
    if (!!res && res['resultCode'] === 210)
    {
      callback(res['count']);
    }
    else
    {
      console.log("Unable to retrieve movie count");
      console.log(res, status, xhr);
      callback(1);
    }
  });
}

function getMoviesBy(params, callback)
{
  makePrivilegedRequest(`movies/search?` + $.param(params), "GET", function(res, status, xhr){
    if (!!res && res['resultCode'] === 210)
    {
      callback(res['movies']);
    }
    else
    {
      console.log("Unable to retrieve any movies");
      console.log(res, status, xhr);
      callback([]);
    }
  });
}

function getStarsBy(params, callback)
{
  makePrivilegedRequest(`movies/star/search?` + $.param(params), "GET", function(res, status, xhr){
    if (!!res && res['resultCode'] === 212)
    {
      callback(res['stars']);
    }
    else
    {
      console.log("Unable to retrieve any stars");
      console.log(res, status, xhr);
      callback([]);
    }
  });
}

function getAllGenres(callback)
{
  makePrivilegedRequest(`movies/genre`, "GET", function(res, status, xhr){
    if (!!res && res['resultCode'] === 219)
    {
      callback(res['genres']);
    }
    else
    {
      console.log("Unable to retrieve any genres");
      console.log(res, status, xhr);
      callback([]);
    }
  });
}
