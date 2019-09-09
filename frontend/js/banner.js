let bannerlinks = ["https://image.tmdb.org/t/p/original/hqkIcbrOHL86UncnHIsHVcVmzue.jpg", "img/avengersmovie.jpg", "img/aladdinmovie.jpg"];
let banner_index = 0;
let banner_ids = [
  "tt0409459", // Watchmen
  "tt0328107", // Man on Fire
  "tt0440963", // Bourne Ultimatum
  "tt0381061", // Casino Royale
  "tt0378194", // Kill Bill Vol. 2
  "tt0416449", // 300
  "tt0468569" // The Dark Knight
];
let banners = [{"movieId":"tt0328107","title":"Man on Fire","director":"Tony Scott","year":2004,"backdrop_path":"/2cBmeSLCyLqkn6YIERKdRUToqsa.jpg","budget":0,"revenue":0,"overview":"Jaded ex-CIA operative John Creasy reluctantly accepts a job as the bodyguard for a 10-year-old girl in Mexico City. They clash at first, but eventually bond, and when she's kidnapped he's consumed by fury and will stop at nothing to save her life.","poster_path":"/qAbRLPe8T7ehKzr1Tgo78T7ASrS.jpg","rating":7.7,"numVotes":286822,"genres":[{"id":1,"name":"Action"},{"id":7,"name":"Crime"},{"id":9,"name":"Drama"}],"stars":[{"id":"nm0000243","name":"Denzel Washington"},{"id":"nm0000686","name":"Christopher Walken"},{"id":"nm0001338","name":"Brian Helgeland"},{"id":"nm0001716","name":"Tony Scott"},{"id":"nm0004581","name":"Harry Gregson-Williams"},{"id":"nm0266824","name":"Dakota Fanning"},{"id":"nm0287946","name":"Lucas Foster"},{"id":"nm0586969","name":"Arnon Milchan"},{"id":"nm0593664","name":"Radha Mitchell"},{"id":"nm0704031","name":"A.J. Quinnell"}],"hidden":false},{"movieId":"tt0409459","title":"Watchmen","director":"Zack Snyder","year":2009,"backdrop_path":"/ok6Ws65nDSLAIKkKgIjqyM5OGCc.jpg","budget":0,"revenue":0,"overview":"In a gritty and alternate 1985 the glory days of costumed vigilantes have been brought to a close by a government crackdown, but after one of the masked veterans is brutally murdered, an investigation into the killer is initiated. The reunited heroes set out to prevent their own destruction, but in doing so uncover a sinister plot that puts all of humanity in grave danger.","poster_path":"/1QqwJBv5a6ddgzaT6cLytJioyrJ.jpg","rating":7.6,"numVotes":421814,"genres":[{"id":1,"name":"Action"},{"id":9,"name":"Drama"},{"id":16,"name":"Mystery"}],"stars":[{"id":"nm0001303","name":"Carla Gugino"},{"id":"nm0015196","name":"Malin Akerman"},{"id":"nm0330383","name":"Lawrence Gordon"},{"id":"nm0355097","name":"Jackie Earle Haley"},{"id":"nm0371684","name":"David Hayter"},{"id":"nm0600872","name":"Alan Moore"},{"id":"nm0811583","name":"Zack Snyder"},{"id":"nm0874844","name":"Alex Tse"},{"id":"nm0933940","name":"Patrick Wilson"},{"id":"nm1733301","name":"Dave Gibbons"}],"hidden":false},{"movieId":"tt0381061","title":"Casino Royale","director":"Martin Campbell","year":2006,"backdrop_path":"/xq6hXdBpDPIXWjtmvbFmtLvBFJt.jpg","budget":0,"revenue":0,"overview":"Le Chiffre, a banker to the world's terrorists, is scheduled to participate in a high-stakes poker game in Montenegro, where he intends to use his winnings to establish his financial grip on the terrorist market. M sends Bond â€“ on his maiden mission as a 00 Agent â€“ to attend this game and prevent Le Chiffre from winning. With the help of Vesper Lynd and Felix Leiter, Bond enters the most important poker game in his already dangerous career.","poster_path":"/zlWBxz2pTA9p45kUTrI8AQiKrHm.jpg","rating":8,"numVotes":506866,"genres":[{"id":1,"name":"Action"},{"id":3,"name":"Adventure"},{"id":21,"name":"Thriller"}],"stars":[{"id":"nm0001132","name":"Judi Dench"},{"id":"nm0001220","name":"Ian Fleming"},{"id":"nm0110483","name":"Barbara Broccoli"},{"id":"nm0132709","name":"Martin Campbell"},{"id":"nm0185819","name":"Daniel Craig"},{"id":"nm0353673","name":"Paul Haggis"},{"id":"nm0701031","name":"Neal Purvis"},{"id":"nm0905498","name":"Robert Wade"},{"id":"nm0942482","name":"Jeffrey Wright"},{"id":"nm1200692","name":"Eva Green"}],"hidden":false},{"movieId":"tt0440963","title":"The Bourne Ultimatum","director":"Paul Greengrass","year":2007,"backdrop_path":"/6WpDOqkZFmhNJ0rwuLJiZVKlZi1.jpg","budget":0,"revenue":0,"overview":"Bourne is brought out of hiding once again by reporter Simon Ross who is trying to unveil Operation Blackbriar, an upgrade to Project Treadstone, in a series of newspaper columns. Information from the reporter stirs a new set of memories, and Bourne must finally uncover his dark past while dodging The Company's best efforts to eradicate him.","poster_path":"/fHho6JYYY0nRcETWSoeI19iZsNF.jpg","rating":8.1,"numVotes":541190,"genres":[{"id":1,"name":"Action"},{"id":16,"name":"Mystery"},{"id":21,"name":"Thriller"}],"stars":[{"id":"nm0000260","name":"Joan Allen"},{"id":"nm0000354","name":"Matt Damon"},{"id":"nm0005466","name":"Julia Stiles"},{"id":"nm0006904","name":"Tony Gilroy"},{"id":"nm0189777","name":"Patrick Crowley"},{"id":"nm0339030","name":"Paul Greengrass"},{"id":"nm0524924","name":"Robert Ludlum"},{"id":"nm1079776","name":"George Nolfi"},{"id":"nm1183149","name":"Edgar Ramírez"},{"id":"nm1994243","name":"Scott Z. Burns"}],"hidden":false},{"movieId":"tt0378194","title":"Kill Bill: Vol. 2","director":"Quentin Tarantino","year":2004,"backdrop_path":"/gFJq6ZvmKrqcf8hWrIcndBUp7SY.jpg","budget":0,"revenue":0,"overview":"The Bride unwaveringly continues on her roaring rampage of revenge against the band of assassins who had tried to kill her and her unborn child. She visits each of her former associates one-by-one, checking off the victims on her Death List Five until there's nothing left to do â€¦ but kill Bill.","poster_path":"/au9lFA5a2ZnBKCzPbZQf00r7J64.jpg","rating":8,"numVotes":559953,"genres":[{"id":1,"name":"Action"},{"id":7,"name":"Crime"},{"id":21,"name":"Thriller"}],"stars":[{"id":"nm0000233","name":"Quentin Tarantino"},{"id":"nm0000235","name":"Uma Thurman"},{"id":"nm0000435","name":"Daryl Hannah"},{"id":"nm0000514","name":"Michael Madsen"},{"id":"nm0001016","name":"David Carradine"},{"id":"nm0001675","name":"Robert Rodriguez"},{"id":"nm0004744","name":"Lawrence Bender"},{"id":"nm0579673","name":"Sally Menke"},{"id":"nm0724744","name":"Robert Richardson"},{"id":"nm0913300","name":"David Wasco"}],"hidden":false},{"movieId":"tt0416449","title":"300","director":"Zack Snyder","year":2006,"backdrop_path":"/oHOTQkTYgDuoCYMaBEzuB9DqguX.jpg","budget":0,"revenue":0,"overview":"Based on Frank Miller's graphic novel, \"300\" is very loosely based the 480 B.C. Battle of Thermopylae, where the King of Sparta led his army against the advancing Persians; the battle is said to have inspired all of Greece to band together against the Persians, and helped usher in the world's first democracy.","poster_path":"/bYR8O1H1ZlME7Dm9ysfTYZnRDpw.jpg","rating":7.7,"numVotes":652330,"genres":[{"id":1,"name":"Action"},{"id":11,"name":"Fantasy"}],"stars":[{"id":"nm0004799","name":"Mark Canton"},{"id":"nm0124930","name":"Gerard Butler"},{"id":"nm0372176","name":"Lena Headey"},{"id":"nm0426500","name":"Kurt Johnstad"},{"id":"nm0588340","name":"Frank Miller"},{"id":"nm0811583","name":"Zack Snyder"},{"id":"nm0920992","name":"David Wenham"},{"id":"nm0922035","name":"Dominic West"},{"id":"nm1290582","name":"Michael B. Gordon"},{"id":"nm2241067","name":"Lynn Varley"}],"hidden":false},{"movieId":"tt0468569","title":"The Dark Knight","director":"Christopher Nolan","year":2008,"backdrop_path":"/hqkIcbrOHL86UncnHIsHVcVmzue.jpg","budget":0,"revenue":0,"overview":"Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.","poster_path":"/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg","rating":9,"numVotes":1855106,"genres":[{"id":1,"name":"Action"},{"id":7,"name":"Crime"},{"id":9,"name":"Drama"}],"stars":[{"id":"nm0000288","name":"Christian Bale"},{"id":"nm0000323","name":"Michael Caine"},{"id":"nm0001173","name":"Aaron Eckhart"},{"id":"nm0004170","name":"Bob Kane"},{"id":"nm0005132","name":"Heath Ledger"},{"id":"nm0333060","name":"David S. Goyer"},{"id":"nm0634240","name":"Christopher Nolan"},{"id":"nm0634300","name":"Jonathan Nolan"},{"id":"nm0650038","name":"Lorne Orleans"},{"id":"nm0746273","name":"Charles Roven"}],"hidden":false}];

function selectBanner()
{
  banner_index = (banner_index + 1) % banners.length;
  return banners[banner_index];
}

function fetchBannerData()
{
  for (let id of banner_ids)
  {
    fetchMovieDetails(id, function(movieData){
      if (!!movieData)
        banners.push(movieData);
    });
  }
}

function nextBanner()
{
  console.log("nextBanner");
  resumeBanner();
  let currentBanner = $(".banner");
  currentBanner.addClass("temp");

  let newBannerData = selectBanner();

  let newBanner = $(`
    <div id="b-${newBannerData["movieId"]}" data-mid="${newBannerData["movieId"]}" class="banner next">
      <div class="banner-text">${newBannerData["title"]}</div>
      <div class="bottombar"></div>
    </div>
  `);
  let url = getImageLink(newBannerData["backdrop_path"]);
  if (determineIfMobile())
  {
    console.log("mobile");
    url = getImageLink(newBannerData["poster_path"]);
  }

  newBanner.css({
    "background-image": "url(" + url + ")"
  });
  setBannerSize(newBanner);
  $(".banner-holder").append(newBanner);
  attachBannerEventListener();
  let img = new Image();
  img.onload = function() {
    currentBanner.animate({
      "left": "-100%"
    }, 1000);
    $(".banner.next").animate({
      "left": "0"
    }, 1000, function(){
      $(this).removeClass("next");
      $(".banner.temp").remove();
    });
  };
  img.src = url;
  if (img.complete)
    img.onload();
}

function attachBannerEventListener()
{
  $(".banner").click((e) => {
    let id = $(e.target).attr("data-mid");
    pauseBanner();
    createPopupLoader();
    fetchMovieDetails(id, (movieData) => {
      let popup = createMoviePopup(movieData);
      changeCloseAction(popup, () => {
        resumeBanner();
      });
    });
  });
}

function pauseBanner()
{
  console.log("pauseBanner")
  paused = true;
  $(".banner").css("filter", "blur(5px)");
}

function resumeBanner()
{
  paused = false;
  $(".banner").css("filter", "none");
}

function setBannerSize(banner)
{
  let h = $(window).height();
  let w = $(window).width();

  if ((h / w) > (1080 / 1920))
  {
    // height needs to be at least 100%
    banner.css("background-size", "auto 100%");
  }
  else
  {
    // width needs to be at least 100%
    banner.css("background-size", "100% auto");
  }
}

let paused = false;
let bannerSlideshow = setInterval(function(){
  if (!paused && banners.length >= 2)
    nextBanner();
}, 5000);
