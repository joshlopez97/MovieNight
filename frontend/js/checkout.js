let customer_info = {};

function displayCheckoutPage()
{
  $(".page-title").text("Checkout");
  renderBlankPageContainer();
  fetchCustomerData((customerData)=>{
    if (customerData === null)
      createBillingPopup("Enter Billing Information");
    else
    {
      fetchCCData(customerData["ccId"], (ccData)=>{
        createBillingPopup(
          "Verify Billing Information",
          convertToFormData(customerData, ccData)
        );
      });
    }
  });
}

function fetchCustomerData(callback)
{
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/customer/retrieve", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3320)
      callback(res["customer"]);
    else
      callback(null);
  }, JSON.stringify({"email": email}));
}

function fetchCCData(ccId, callback)
{
  makePrivilegedRequest("billing/creditcard/retrieve", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3230)
      callback(res["creditcard"]);
    else
      callback(null);
  }, JSON.stringify({"id": ccId}));
}

function convertToFormData(customerData, ccData)
{
  return {
    "firstName": customerData["firstName"],
    "lastName": customerData["lastName"],
    "address": customerData["address"],
    "cardnumber": customerData["ccId"].slice(0, 16),
    "cvv": customerData["ccId"].slice(16),
    "exp": ccData["expiration"]
  };
}

function createBillingPopup(title, data=null)
{
  let popup = createPopup(title, "Checkout");
  addField(popup, "First Name", "text", "", "firstName");
  addField(popup, "Last Name", "text", "", "lastName");
  addField(popup, "address", "text", "");
  $(".popup-form").addClass("big-form");
  $(`
    <div class="cc-info">
      <h3>Credit Card</h3>
      <div class="popup-form-field">
        <label class="popup-form-field-label big-side-label" for="ccnum">Card Number</label>
        <label class="popup-form-field-label small-side-label" for="cvv">CVV</label>
        <input class="big-side-text" type="text" id="ccnum" name="cardnumber">
        <input class="small-side-text" type="text" id="cvv" name="cvv">
      <div>
      <div class="popup-form-field">
        <div class="popup-form-field-label">Expiration</div>
        <input class="popup-form-field-input" type="text" name="exp">
      </div>
    </div>
  `).insertBefore(".popup-submit");
  if (data !== null)
  {
    assignValues(popup, data);
  }
  addSubmitAction(popup, (e)=>{
    e.preventDefault();
    let formData = getFormData(popup);
    let ccInfo = {
      "id": formData["cardnumber"]+formData["cvv"],
      "expiration": formData["exp"],
      "firstName": formData["firstName"],
      "lastName": formData["lastName"]
    };
    insertCC(ccInfo, (ccInserted) => {
      if (ccInserted)
      {
        let customerInfo = {
          "firstName": formData["firstName"],
          "lastName": formData["lastName"],
          "address": formData["address"],
          "ccId": ccInfo["id"]
        };
        customerInfo["email"] = JSON.parse(getCookie())['email'];
        insertCustomer(customerInfo, (customerInserted) => {
          if (customerInserted)
          {
            placeOrder(customerInfo["email"]);
          }
        });
      }
    });
    return false;
  });
}

function placeOrder(email)
{
  makePrivilegedRequest("billing/order/place", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3400)
    {
      destroyPopup();
      notice(res["message"]);
      window.open(res["redirectURL"]);
    }
    else
    {
      console.log(res, status, xhr);
      notice("Unable to place order");
    }
  }, JSON.stringify({"email": email}));
}

function updateCustomer(customerInfo, callback)
{
  makePrivilegedRequest("billing/customer/update", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3310)
      return callback(true);
    else
    {
      notice(res["message"]);
      return callback(false);
    }
  }, JSON.stringify(customerInfo));
}

function insertCustomer(customerInfo, callback)
{
  makePrivilegedRequest("billing/customer/insert", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3300)
      return callback(true);
    else if (!!res && res["resultCode"] === 333)
      return updateCustomer(customerInfo, callback);
    else
    {
      notice(res["message"]);
      return callback(false);
    }
  }, JSON.stringify(customerInfo));
}

function updateCC(ccInfo, callback)
{
  makePrivilegedRequest("billing/creditcard/update", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3210)
      return callback(true);
    else
    {
      notice(res["message"]);
      return callback(false);
    }
  }, JSON.stringify(ccInfo));
}

function insertCC(ccInfo, callback)
{
  makePrivilegedRequest("billing/creditcard/insert", "POST", function(res, status, xhr){
    if (!!res && res["resultCode"] === 3200)
      return callback(true);
    else if (!!res && res["resultCode"] === 325)
      return updateCC(ccInfo, callback);
    else
    {
      notice(res["message"]);
      return callback(false);
    }
  }, JSON.stringify(ccInfo));
}
