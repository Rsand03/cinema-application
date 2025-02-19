const BACKEND_URL = 'http://localhost:8080/api';


/**
 * Requests all movie data from back-end.
 * If successful, renders the movie selection form with renderMovieForm() function.
 */
async function fetchMovieData() {

    const url = BACKEND_URL + '/movies';
    const response = await fetch(url, {
        method: 'GET',
    });

    if (response.status !== 200) {
        // display error
    } else {
        const data = await response.json();
        // create form with radio buttons
        // {"id": movie session id, "asString": all necessary movie data as a pre-formatted string}
        renderMoviesForm(data);
    }
}


/**
 * Requests data of movies that match the filtering criteria.
 * If successful, renders the movie selection form with renderMovieForm() function.
 * Filtering options:
 * genre, age rating, "later than" session starting time, language.
 */
async function fetchFilteredMoviesData() {
    const genre = document.getElementById("genre-selection").value;
    const ageRating = document.getElementById("age-rating-selection").value;
    const sessionStartingTime = document.getElementById("session-starting-time-selection").value;
    const language = document.getElementById("language-selection").value;

    const params = [];
    if (genre) params.push(`genre=${genre}`);
    if (ageRating) params.push(`ageRating=${ageRating}`);
    if (sessionStartingTime) params.push(`sessionStartTime=${sessionStartingTime}`);
    if (language) params.push(`language=${language}`);

    const url = BACKEND_URL + "/movies/filtered?" + params.join('&');
    const response = await fetch(url, {
        method: 'GET'
    });
    if (response.status !== 200) {
        displayErrorMessage("Something went wrong." + response.status);
    } else {
        const data = await response.json();
        // create form with radio buttons
        renderMoviesForm(data);
    }
}


/**
 * Requests recommended movies data from back-end based on the current user.
 * If successful, renders the movie selection form with renderMovieForm() function.
 */
async function fetchRecommendedMovies() {
    const url = BACKEND_URL + "/movies/recommended"
    const response = await fetch(url, {
        method: 'GET'
    });
    if (response.status !== 200) {
        displayErrorMessage("Unable to recommend movies.");
    } else {
        const data = await response.json();
        // create form with radio buttons
        renderMoviesForm(data);
    }
}


/**
 * Renders the movie selection form.
 * If there are no movies to render, displays error message.
 * @param {list} movieData movie options to be rendered
 * movieData: {"id": movie session id, "asString": all necessary movie data as a pre-formatted string}
 */
function renderMoviesForm(movieData) {
    const moviesSelectionField = document.getElementById('movie-selection-container');
    let displayedMovies = "";
    for (const movie of movieData) {
        const displayedLabel = movie.asString;
        displayedMovies +=
            `<div class="movie-selection-input-element">
                <input type="radio" class="media" id="movie${movie.id}" name="movie" value="${movie.id}" style="margin-left: 1rem">
                <label for="movie${movie.id}">
                    <pre style="font-family: Roboto Mono, monospace; margin: 0">${displayedLabel}</pre>
                </label>
            </div>`
    }
    if (displayedMovies === "") {
        displayErrorMessage("No matching movies");
    } else {
        displayErrorMessage("");  // stop showing previous error message
        moviesSelectionField.innerHTML = displayedMovies;
    }
}


/**
 * Process data of a specified form.
 * Retrieves selected option data.
 * @param {string} form_id id of the form
 */
function processFormData(form_id) {
    if (form_id === "movie-selection-form") {
        const selectedMovie = document.querySelector('input[name="movie"]:checked');
        if (selectedMovie !== null) {
            return selectedMovie.value;  // selected movie id
        } else {
            return null;
        }
    }
}


/**
 * Display error message on home page.
 * @param {string} messageText text to display
 */
function displayErrorMessage(messageText) {
    const errorMessageBox = document.getElementById("error-message-box");
    errorMessageBox.innerText = messageText;
}


/**
 * Verify the selection of a movie and pass ticket count to seating page.
 * Back-end adds the movie to user's watching history.
 * Uses processFormData() to get the id of the selected movie.
 */
async function verifyMovieSelection() {
    const movieId = processFormData("movie-selection-form");
    const ticketCount = document.getElementById("ticket-selection").value;

    if (movieId === null) {
        displayErrorMessage("Please select a movie.");
    } else {
        const url = BACKEND_URL + '/movies/selection' + '?id=' + movieId;
        const response = await fetch(url, {
            method: 'PATCH'
        });
        if (response.status !== 200) {
            displayErrorMessage("Something went wrong. Please try again.");
        } else {
            window.location.href = `seating-plan.html?ticket_count=${ticketCount}`;
        }
    }
}


/**
 * Fetch dropdown menu content from backend and create the dropdown menus.
 */
async function fetchFilteringOptions() {

    const url = BACKEND_URL + '/movies/attributes';
    const response = await fetch(url, {
        method: 'GET'
    });
    if (response.status !== 200) {
        displayErrorMessage("Couldn't load dropdown menu content");
    } else {
        const data = await response.json();
        createFilteringDropdownMenus(data);
    }
}


/**
 * Generate HTML option elements for a dropdown menu based on the provided options data.
 *
 * @param optionsData - Array of options to populate the dropdown menu.
 * @returns {string} String containing the generated HTML option elements.
 */
function createFormOptions(optionsData) {
    let formOptions = `<option selected></option>`
    for (const option of optionsData) {
        formOptions +=
            `<option value="${option}">${option}</option>`
    }
    return formOptions;
}


/**
 * Create dropdown menus for selecting movie filtering parameters.
 * @param {json} movieAttributes text to display
 */
function createFilteringDropdownMenus(movieAttributes) {
    const genreDropdown = document.getElementById("genre-selection");
    const ageRatingDropdown = document.getElementById("age-rating-selection");
    const startingTimeDropdown = document.getElementById("session-starting-time-selection");
    const languageDropdown = document.getElementById("language-selection");

    genreDropdown.innerHTML = createFormOptions(movieAttributes.genres);
    ageRatingDropdown.innerHTML = createFormOptions(movieAttributes.ageRatings);
    startingTimeDropdown.innerHTML = createFormOptions(movieAttributes.sessionStartingTimes);
    languageDropdown.innerHTML = createFormOptions(movieAttributes.languages);
}
