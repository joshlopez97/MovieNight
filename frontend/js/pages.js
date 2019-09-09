let currentPage = 1;

function adjustPages()
{
  if (currentPage !== 1)
  {
    currentParams["offset"] = currentParams["limit"] * (currentPage - 1);
  }
}

function fixPages(count)
{
    currentPage = Math.ceil(parseFloat(count) / currentParams["limit"]);
    currentParams["offset"] = (currentPage - 1) * currentParams["limit"];
    displayMoviesCurrentParams();
}

function resetPages()
{
  currentPage = 1;
  currentParams["offset"] = 0;
  currentParams["limit"] = 10;
}

function createStarPages(stars)
{
  let pageHolder = $(".page-holder");
  if (pageHolder.length === 0)
  {
    pageHolder = $(`
      <div class="page-holder">
      </div>
    `);
  }
  pageHolder.empty();

  let pb = createPrevPageBtn();
  pageHolder.append(pb);
  pageHolder.append("<span style='font-size: 12pt;padding: 0 10px;'>" + currentStarPage + "</span>");
  let nb = createNextPageBtn()
  pageHolder.append(nb);
  if (starParams["offset"] <= 0)
  {
    pb.addClass("disabled");
    pb.prop("disabled", true);
  }
  if (stars.length < starParams["limit"])
  {
    nb.addClass("disabled");
    pb.prop("disabled", true);
  }
  return pageHolder;
}

function createPages()
{
  let pageHolder = $(".page-holder");
  if (pageHolder.length === 0)
  {
    pageHolder = $(`
      <div class="page-holder">
        Loading...
      </div>
    `);
  }
  getTotalMovieCount(function(count)
  {
    console.log("count = " + count);
    if (currentParams["offset"] >= count)
    {
      fixPages(count);
    }
    else
    {
      pageHolder.empty();
      pageHolder.append(createPageBtn(currentPage).addClass("current-page-btn"));
      let numPages = Math.ceil(parseFloat(count) / currentParams["limit"]);
      if (numPages > 1)
      {
        if (currentPage > 1)
        {
          pageHolder.prepend(createPageBtn(currentPage - 1));
        }
        if (currentPage > 3)
        {
          pageHolder.prepend("<span style='font-size: 15pt;'>...</span>");
        }
        pageHolder.prepend(createPageBtn(1));

        if (currentPage + 1 < numPages)
        {
          pageHolder.append(createPageBtn(currentPage + 1));
        }
        if (currentPage + 2 < numPages && currentPage === 1)
        {
          pageHolder.append(createPageBtn(currentPage + 2));
        }
        if (currentPage + 3 <= numPages)
        {
          pageHolder.append("<span style='font-size: 15pt;'>...</span>");
        }
        pageHolder.append(createPageBtn(numPages));
      }
    }
  });
  return pageHolder;
}

function createPageBtn(pageNo)
{
  let existingPageBtn = $(`#p${pageNo}`);
  if (existingPageBtn.length === 0)
  {
    let btn = $(`<button id="p${pageNo}" class="page-btn">${pageNo}</button>`);
    btn.click(function()
    {
      currentPage = pageNo;
      currentParams["offset"] = (pageNo - 1) * currentParams["limit"];
      displayMoviesCurrentParams();
    });
    return btn;
  }
  else
  {
    return existingPageBtn;
  }
}

// STAR PAGES ONLY
function createNextPageBtn()
{
  let existingPageBtn = $(`#next-page`);
  if (existingPageBtn.length === 0)
  {
    let btn = $(`<button id="next-page" class="page-btn">Next</button>`);
    btn.click(function()
    {
      currentStarPage++;
      starParams["offset"] += starParams["limit"];
      displayStarsCurrentParams()
    });
    return btn;
  }
  else
  {
    return existingPageBtn;
  }
}
function createPrevPageBtn()
{
  let existingPageBtn = $(`#prev-page`);
  if (existingPageBtn.length === 0)
  {
    let btn = $(`<button id="prev-page" class="page-btn">Prev</button>`);
    btn.click(function()
    {
      if (currentStarPage > 1 && starParams["offset"] > 0)
      {
        currentStarPage--;
        starParams["offset"] -= starParams["limit"];
        displayStarsCurrentParams()
      }
    });
    return btn;
  }
  else
  {
    return existingPageBtn;
  }
}