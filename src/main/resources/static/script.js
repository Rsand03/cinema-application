
const BACKEND_URL = 'http://localhost:8080';


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

    const url = BACKEND_URL + "/movies/filtered" +
        "?genre=" + genre +
        "&ageRating=" + ageRating +
        "&sessionStartTime=" + sessionStartingTime +
        "&language=" + language;
    const response = await fetch(url,{
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
    const response = await fetch(url,{
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
 * @param {string} message_text text to display
 */
function displayErrorMessage(message_text) {
    const errorMessageBox = document.getElementById("error-message-box");
    errorMessageBox.innerText = message_text;
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
        const response = await fetch(url,  {
            method: 'POST'
        });
        if (response.status !== 200) {
            displayErrorMessage("Something went wrong. Please try again.");
        } else {
            window.location.href = `seating-plan.html?ticket_count=${ticketCount}`;
        }
    }
}


/**
 * Generates the content of a filtering options dropdown menu.
 * TODO: Make all dropdown menus dynamic by requesting the content data from back-end.
 */
function createTimeFilteringDropdown() {
    const dropdownMenu = document.getElementById("session-starting-time-selection");
    let dropdownContent = "";
    for (let i = 8; i < 23; i++) {
        if (dropdownContent === "") {
            dropdownContent += `<option value="-" selected>-</option>`
        }
        // converting value to string enables easier data processing in back-end movies filtering function
        dropdownContent += `<option value=${i.toString()}>${i}</option>`
    }
    dropdownMenu.innerHTML = dropdownContent;
}
