function createRatingStars(rating, numVotes, star_width=90)
{
  let yellow_size = star_width * (rating / 10);
  let grey_size = star_width - yellow_size;
  let ratingContainer = $(`<div class="rating"></div>`);
  let yellowStars = $(`<div class="yellow stars"></div>`).css({
    width: yellow_size + "px",
    "height": (star_width * 0.2) + "px",
    "background-size": star_width + "px"
  });
  let greyStars = $(`<div  class="grey stars"></div>`).css({
    width: grey_size + "px",
    "margin-left": yellow_size + "px",
    "height": (star_width * 0.2) + "px",
    "background-size": star_width + "px"
  });
  ratingContainer.append(yellowStars, greyStars);
  if (numVotes !== null)
  {
    let numVotesText = $(`<div class="numVotes">${numVotes} ratings</div>`).css({
      "font-size": (star_width/12) + "pt",
      "margin-left": star_width + "px",
      "padding": (star_width/24) + "px"
    });
    ratingContainer.append(numVotesText);
  }
  return ratingContainer;
}

function createRatingPrompt(movieId)
{
  let star_width = 350;
  let sec_width = star_width / 10;
  let ratingDialog = $(`<div class="rating-dialog"></div>`);
  let bigRating = createRatingStars(0, null, star_width).addClass("big-rating").css("width", star_width + "px");
  let left = sec_width * -1;
  for (let i = 0; i < 11; i++)
  {
    let section = $(`<div id=${i} class="rating-section"></div>`).css({
      "height": (star_width * 0.2) + "px",
      "width": sec_width + "px",
      "left": left
    });
    section.hover(function(el){
      let sec_left = parseInt($(el.target).css("left"));
      bigRating.find(".yellow.stars").css({
        "width": (sec_left + sec_width) + "px"
      });
      bigRating.find(".grey.stars").css({
        "width": (star_width - (sec_left + sec_width)) + "px",
        "margin-left": (sec_left + sec_width) + "px"
      });
      $(".rating-dialog-text").text($(el.target).attr("id"));
    });
    section.click(function(el)
    {
      makePrivilegedRequest(
        "movies/rating",
        "POST",
        function(res, status, xhr){
          if (!!res && res['resultCode'] === 250)
          {
            $(".rating-dialog-subtext").text("Rating submitted.").fadeIn(300);
          }
          else
          {
            $(".rating-dialog-subtext").text("Unable to submit rating.").fadeIn(300);
          }
          setTimeout(function()
          {
            $(".rating-dialog").fadeOut(500, () => $(this).remove());
          }, 700);
        },
        JSON.stringify({"id": movieId, "rating": $(el.target).attr("id")}),
        {"Content-Type": "application/json"}
      );
    });
    left += sec_width;
    bigRating.prepend(section);
  }
  ratingDialog.append("<div class='rating-dialog-text'></div>");
  ratingDialog.append("<div class='rating-dialog-subtext'></div>");
  ratingDialog.append(bigRating);
  ratingDialog.append(`
    <div id="close-rating-dialog" class="close-icon-holder">
      <img class="close-icon" src="img/close.png" alt="close">
    </div>
  `);
  $("body").append(ratingDialog);
  $("#close-rating-dialog").click(function(){
    closeRatingDialog();
  });
}

function closeRatingDialog()
{
  $(".rating-dialog").fadeOut(()=>$(this).remove());
}