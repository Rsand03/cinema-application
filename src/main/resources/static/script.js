const BACKEND_URL = 'http://localhost:8080';


/**
 * Requests all movie data from back-end.
 * If successful, renders the movie selection form with renderMovieForm() function.
 */
async function loadMovieData() {

    const url = BACKEND_URL + '/movies';
    const response = await fetch(url, {
        method: 'GET',
    });

    if (response.status !== 200) {
        // display error
    } else {
        const data = await response.json();
        // create form with radio buttons
        renderMoviesForm(data);
    }
}


/**
 * Requests data of movies that match the filtering criteria.
 * If successful, renders the movie selection form with renderMovieForm() function.
 * Filtering options:
 * genre, age rating, "later than" session starting time, language.
 */
async function loadFilteredMoviesData() {
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
async function loadRecommendedMovies() {
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
 * @param movieData movie options to be rendered
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
 * @param form_id id of the form
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
 * @param message_text text to display
 */
function displayErrorMessage(message_text) {
    const errorMessageBox = document.getElementById("error-message-box");
    errorMessageBox.innerText = message_text;
}


/**
 * Display error message on seating plan page.
 * @param message_text text to display
 */
function displaySeatErrorMessage(message_text) {
    const errorMessageBox = document.getElementById("seats-error-message-box");
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
            window.location.href = `./seats.html?ticket_count=${ticketCount}`;
        }
    }
}


/**
 * Generates the content of a filtering options dropdown menu.
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


/**
 * Requests seating plan data from back-end.
 * Displays notification in case no neighbouring seats are available or no seats are available at all.
 */
async function loadSeatingPlan() {
    const params = new URLSearchParams(window.location.search);
    const ticketCount = params.get('ticket_count');

    const url = BACKEND_URL + "/seats" + "?ticketCount=" + ticketCount;
    const response = await fetch(url,  {
        method: 'GET'
    });

    const data = await response.json();
    if (response.status !== 200 && response.status !== 206) {
        displaySeatErrorMessage("No available seats.");
    } else if (response.status === 206) {
        displaySeatErrorMessage("Unfortunately no neighbouring seats were available.");
        renderSeatingPlan(data);
    } else {
        renderSeatingPlan(data);
    }
}

/**
 * Renders the seating plan based on seating plan data.
 * @param seatingPlanData seating plan data in json format
 */
function renderSeatingPlan(seatingPlanData) {
    for (const seat of seatingPlanData) {
        const seatingPlanField = document.getElementById('seating-plan');
        // create form with radio buttons
        let displayedSeats = "";

        for (const seat of seatingPlanData) {
            const seatNumber = seat.seatNumber;
            let background_color;
            switch (seat.occupationStatus) {
                case "FREE": background_color = "lightgray"; break;
                case "OCCUPIED": background_color = "Red"; break;
                case "SELECTED": background_color = "Green"; break;
            }
            displayedSeats += `<div class="seat" style="background-color: ${background_color}">${seatNumber}</div>`
        }
        seatingPlanField.innerHTML = displayedSeats;
    }
}
