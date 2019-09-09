function getLoader()
{
  return `
    <div class="loader-holder">
      <div class="loader">
        <div class="lds-css ng-scope">
          <div style="width:100%;height:100%" class="lds-bars">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </div>
        </div>
      </div>
    </div>
  `;
}

function removeLoader()
{
  $(".loader-holder").remove();
}

function loaderIsPresent(elem)
{
  return elem.find(".loader-holder").length > 0;
}