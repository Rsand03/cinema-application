const BACKEND_URL = 'http://localhost:8080';


async function load_movie_data() {

    const movies_selection_field = document.getElementById('movie-selection-box');
    const url = BACKEND_URL + '/movies';
    const response = await fetch(url, {
        method: 'GET',
    });

    if (response.status !== 200) {
        // display error
    } else {
        const data = await response.json();
        // create form with radio buttons
        let displayed_movies = "";
        for (const movie of data) {
            const displayed_label = movie.asString;
            displayed_movies +=
                `<div class="movie-selection-input-element">
                    <input type="radio" class="media" name="movie" value="${movie.id}"> ${displayed_label}</input>
                </div>`
        }
        movies_selection_field.innerHTML = displayed_movies;
    }
}

async function load_filtered_movies_data() {
    const genre = document.getElementById("genre-selection").value;
    const age_rating = document.getElementById("age-rating-selection").value;
    const session_starting_time = document.getElementById("session-starting-time-selection").value;
    const language = document.getElementById("language-selection").value;

    const url = BACKEND_URL + "/movies/filtered" +
        "?genre=" + genre +
        "&ageRating=" + age_rating +
        "&sessionStartTime=" + session_starting_time +
        "&language=" + language;
    const response = await fetch(url,{
        method: 'GET'
    });
    if (response.status !== 200) {
        display_error_message("Something went wrong. \n" + response.status);
    } else {
        const movies_selection_field = document.getElementById('movie-selection-box');
        const data = await response.json();
        // create form with radio buttons
        let displayed_movies = "";
        for (const movie of data) {
            const displayed_label = movie.asString;
            displayed_movies +=
                `<div class="movie-selection-input-element">
                    <input type="radio" class="media" name="movie" value="${movie.id}"> ${displayed_label}</input>
                </div>`
        }
        console.log(displayed_movies)
        if (displayed_movies === "") {
            display_error_message("No matching movies");
        } else {
            display_error_message("");  // stop showing previous error message
            movies_selection_field.innerHTML = displayed_movies;
        }
    }
}

function process_form_data(form_id) {
    if (form_id === "movie-selection-form") {
        const selected_movie = document.querySelector('input[name="movie"]:checked');
        if (selected_movie !== null) {
            return selected_movie.value;  // chosen movie id
        } else {
            return null;
        }
    }
}

function display_error_message(message_text) {
    const error_message_box = document.getElementById("error-message-box");
    error_message_box.innerText = message_text;

}

async function verify_movie_selection() {
    const movie_id = process_form_data("movie-selection-form");
    const ticket_count = document.getElementById("ticket-selection").value;

    if (movie_id === null) {
        display_error_message("Please select a movie.");
    } else {
        const url = BACKEND_URL + '/movies/selection' + '?id=' + movie_id;
        const response = await fetch(url,  {
            method: 'POST'
        });
        if (response.status !== 200) {
            display_error_message("Something went wrong. Please try again.");
        } else {
            window.location.href = `./seats.html?ticket_count=${ticket_count}`;
        }
    }
}

function create_time_filtering_dropdown() {
    const dropdown_menu = document.getElementById("session-starting-time-selection");
    let dropdown_content = "";
    for (let i = 8; i < 23; i++) {
        if (dropdown_content === "") {
            dropdown_content += `<option value="-" selected>-</option>`
        }
        // converting value to string enables easier data processing in back-end movies filtering function
        dropdown_content += `<option value=${i.toString()}>${i}</option>`
    }
    dropdown_menu.innerHTML = dropdown_content;
}

async function load_seating_plan() {
    const params = new URLSearchParams(window.location.search);
    const ticket_count = params.get('ticket_count');

    const url = BACKEND_URL + "/seats" + "?ticketCount=" + ticket_count;
    const response = await fetch(url,  {
        method: 'GET'
    });
    if (response.status !== 200) {
        display_error_message("No available seats.");
    } else if (response.status !== 666) {
        const data = await response.json();
        render_seating_plan(data);
    }
}

function render_seating_plan(response_json_data) {

    for (const seat of response_json_data) {
        const seating_plan_field = document.getElementById('seating-plan');
        // create form with radio buttons
        let displayed_seats = "";
        for (const seat of response_json_data) {
            const seat_number = seat.seatNumber;
            let background_color;
            switch (seat.occupationStatus) {
                case "FREE": background_color = "Gray"; break;
                case "OCCUPIED": background_color = "Red"; break;
                case "SELECTED": background_color = "Green"; break;
            }
            displayed_seats +=
                `<div class="seat" style="background-color: ${background_color}">${seat_number}</div>`
        }
        seating_plan_field.innerHTML = displayed_seats;
    }

}
