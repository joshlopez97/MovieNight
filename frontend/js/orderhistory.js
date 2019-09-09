function displayOrderHistory()
{
  $(".page-title").text("Order History");
  let shoppingCartContainer = renderLoadingPageContainer();
  let orderhist = $(".orderhist");
  if (orderhist.length === 0)
  {
    orderhist = $(`
    <div class="orderhist">
    </div>
  `);
    shoppingCartContainer.append(orderhist);
  }
  condenseActivePageElements([".orderhist"]);
  retrieveOrders(function(transactions)
  {
    removeLoader();
    transactions.sort(transactionSort);
    orderhist.empty();
    if (transactions.length === 1)
      $(".page-title").text(`Order History (${transactions.length} Transaction)`);
    else
      $(".page-title").text(`Order History (${transactions.length} Transactions)`);
    for (let transaction of transactions)
    {
      displayTransaction(transaction);
    }
    if (transactions.length === 0)
    {
      orderhist.append("<div class='results-notice'>No Order History</div>")
    }
  });
}

function transactionSort(t1, t2)
{
  let d1 = new Date(t1["create_time"]);
  let d2 = new Date(t2["create_time"]);
  return (d1 < d2) - (d1 > d2);
}


function retrieveOrders(callback)
{
  let email = JSON.parse(getCookie())['email'];
  makePrivilegedRequest("billing/order/retrieve", "POST", function(res, status, xhr){
    if (!!res && res['resultCode'] === 3410)
    {
      callback(res['transactions']);
    }
    else
      callback([]);
  }, JSON.stringify({"email": email}));
}

function getReadableTime(date) {
  let hrs = date.getHours(),
    mins = date.getMinutes(),
    ampm = hrs >= 12 ? 'pm' : 'am';
  hrs = hrs % 12;
  hrs = hrs ? hrs : 12;
  mins = mins < 10 ? '0' + mins : mins;
  return hrs + ':' + mins + ' ' + ampm;
}

function displayTransaction(transaction)
{
  console.log(JSON.stringify(transaction));
  let parsedDate = new Date(transaction["create_time"]);
  let formattedDate = parsedDate.toDateString() + " " + getReadableTime(parsedDate);
  let transactionElem = $(`
    <div class="transaction-holder" id="${transaction["transactionId"]}">
      <div class="transaction-header">
        <span class="transaction-date">${formattedDate}</span>
      </div>
    </div>
  `);
  let itemNo = 1;
  for (let item of transaction["items"])
  {
    transactionElem.append(createOrderItem(item));
    itemNo++;
  }

  transactionElem.append(createTotal(transaction));
  $(".orderhist").append(transactionElem);
}

function createTotal(transaction)
{
  return $(`
    <div class="stacked-item">
      <div style="width: calc(100% - 10px);"class="stacked-item-content">
        <div class="price-breakdown">
          <div class="transaction-price">${"$" + parseFloat(transaction["transaction_fee"]["value"]).toFixed(2)}</div><br>
          <div class="transaction-price">${"$" + (parseFloat(transaction["amount"]["total"]) + parseFloat(transaction["transaction_fee"]["value"])).toFixed(2)}</div>
        </div>
        <div class="price-labels-holder">
          <div class="transaction-label">PayPal Transaction Fee: </div><br>
          <div class="transaction-label">Total: </div>
        </div>
      </div>
    </div>
  `);
}

function createOrderItem(item)
{
  let price = item["unit_price"];
  let disc = item["discount"];
  let quant = item["quantity"];
  let total = (quant * price).toFixed(2) - ((1 - disc) * (price * quant)).toFixed(2);
  let id = item["movieId"];
  let itemElem = $(`
    <div class="stacked-item clickable" id="${id}">
      <div class="movie-thumbnail-holder-small">
        <img id="mt-${id}" class="movie-thumbnail-small" src="img/default-movie-icon.png" alt="">
      </div>
      <div class="stacked-item-content">
        <div class="movie-header-holder">
          <span class="movie-title">Loading...</span>
        </div>
        <div class="price-breakdown">
          <div class="transaction-price">${quant + " × $" + price.toFixed(2) + " = $" + (quant * price).toFixed(2)}</div><br>
          <div class="transaction-price">${"&#8722; $" + ((1 - disc) * (price * quant)).toFixed(2)}</div><br>
          <div class="transaction-price">${"$" + total.toFixed(2)}</div>
        </div>
        <div class="price-labels-holder">
          <div class="transaction-label">&nbsp;</div><br>
          <div class="transaction-label">58% Discount: </div><br>
          <div class="transaction-label total">Total: </div>
        </div>
      </div>
    </div>
  `);
  itemElem.click(movieClickHandler);
  fetchMovieDetails(id, setMovieDetails);
  return itemElem;
}
