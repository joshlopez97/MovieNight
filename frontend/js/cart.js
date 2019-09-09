

function displayShoppingCart()
{
  $(".page-title").text("Shopping Cart");
  let shoppingCartContainer = renderLoadingPageContainer();
  let cart = $(".shoppingcart");
  if (cart.length === 0)
  {
    cart = $(`
    <div class="shoppingcart">
    </div>
  `);
    shoppingCartContainer.append(cart);
  }
  condenseActivePageElements([".shoppingcart"]);
  retrieveCart(function(items)
  {
    removeLoader();
    cart.empty();
    if (items.length === 1)
      $(".page-title").text(`Shopping Cart (${items.length} Item)`);
    else
      $(".page-title").text(`Shopping Cart (${items.length} Items)`);
    for (let item of items)
    {
      displayCartItem(item);
    }
    if (items.length === 0)
    {
      cart.append("<div class='results-notice'>Cart is empty</div>")
    }
    else
    {
      createClearCartBtn();
      createCheckoutBtn();
    }
  });
}

function createClearCartBtn()
{
  $(".shoppingcart").prepend(`<div id="clear-cart" class="link">Clear Cart</div>`);
  $("#clear-cart").click(clearCart);
}

function createCheckoutBtn()
{
  $(".shoppingcart").append(`
      <div class="checkout-btn-holder">
        <button id="checkout-btn">Checkout</button>
      </div>
    `);
  $("#checkout-btn").click(displayCheckoutPage);
}

function displayCartItem(item)
{
  let id = item["movieId"];
  $(".shoppingcart").append(`
    <div class="stacked-item clickable" id="${id}">
      <div class="movie-thumbnail-holder">
        <img id="mt-${id}" class="movie-thumbnail" src="img/default-movie-icon.png" alt="">
      </div>
      <div class="delete-btn-holder">
        <button id="delete-${id}" class="delete-btn">Remove</button>
      </div>
      <div class="movie-holder-content">
        <div class="movie-header-holder">
          <span class="movie-title">Loading...</span>
        </div>
        <div class="movie-details">
          <div class="movie-detail-holder quantity">
            Quantity: <input id="change-${id}" type="number" class="quantity" value="${item['quantity']}">
            <div class="submit-quantity-holder">
              <button class="submit-quantity" id="submit-change-${id}">Update</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `);
  $("#delete-" + id).click(() => removeMovieFromCart(id));
  $("#change-" + id).focus(() => {
    let submitBtn = $("#submit-change-" + id).css("visibility", "visible");
    submitBtn.click(()=>changeQuantityMovieInCart(id, $("#change-" + id).val()));
  });
  $("#change-" + id).blur(() => setTimeout(()=>$("#submit-change-" + id).css("visibility", "hidden"),100));
  fetchMovieDetails(id, setMovieDetails);
}

function setMovieDetails(movieData)
{
  $("#" + movieData['movieId']).find(".movie-title").text(movieData['title']);
  $("#mt-" + movieData["movieId"]).attr("src", "https://image.tmdb.org/t/p/w500" + movieData["poster_path"]);
}

function retrieveCart(callback)
{
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/cart/retrieve", "POST", function(res, status, xhr){
    if (!!res && res['resultCode'] === 3130)
    {
      callback(res['items']);
    }
    else
      callback([]);
  }, JSON.stringify({"email": email}));
}

function removeMovieFromCart(movieId)
{
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/cart/delete", "POST", function(res, status, xhr){
    console.log(status, res, xhr);
    if (!!res && res['resultCode'] === 3120)
    {
      $("#" + movieId).fadeOut(() => $(this).remove());
      notice(`Item removed from Shopping Cart.`);
    }
    else if (!!res && res['resultCode'] === 312)
    {
      notice(`Item not found in Shopping Cart.`);
    }
    else
    {
      notice(`Unable remove item from Shopping Cart.`);
    }
  }, JSON.stringify({"email": email, "movieId": movieId}));
}

function changeQuantityMovieInCart(movieId, newQuantity)
{
  console.log("q = " + newQuantity)
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/cart/update", "POST", function(res, status, xhr){
    if (!!res && res['resultCode'] === 3110)
    {
      notice(`Item quantity updated to ${newQuantity}.`);
    }
    else if (!!res && (res['resultCode'] === 312 || res['resultCode'] === 33))
    {
      notice(res['message']);
    }
    else
    {
      console.log(status, res, xhr);
      notice(`Unable to update quantity of item.`);
    }
  }, JSON.stringify({"email": email, "movieId": movieId, "quantity": newQuantity}));
}

function clearCart()
{
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/cart/clear", "POST", function(res, status, xhr){
    if (!!res && res['resultCode'] === 3140)
    {
      notice(`Shopping Cart has been cleared.`);
      $(".stacked-item").fadeOut(() => $(this).remove());
      returnToHome();
    }
    else
    {
      console.log(status, res, xhr);
      notice(`Unable to clear Shopping Cart.`);
    }

  }, JSON.stringify({"email": email}));
}

function addMovieToCart(movieId, title, quantity, callback=()=>{})
{
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/cart/insert", "POST", function(res, status, xhr){
    if (!!res && res['resultCode'] === 3100)
    {
      notice(`Item '${title}' has been added to Shopping Cart.`);
    }
    else if (!!res && res['resultCode'] === 311)
    {
      notice(`Item '${title}' already exists in Shopping Cart.`);
    }
    else
    {
      console.log(status, res, xhr);
      notice(`Unable to add item '${title}' to Shopping Cart.`);
    }
    callback();
  }, JSON.stringify({"email": email, "movieId": movieId, "quantity": quantity}));
}