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
        // display error
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
        movies_selection_field.innerHTML = displayed_movies;
    }


}

function process_form_data(form_id) {
    const form = document.getElementById(form_id);
    const form_data = new FormData(form);

    if (form_id === "movie-selection-form") {
        const selected_movie = document.querySelector('input[name="movie"]:checked');
        if (selected_movie !== null) {
            const chosen_movie_id = selected_movie.value;
            return chosen_movie_id
        } else {
            return null;
        }

    }
}

async function verify_movie_selection() {
    const movie_id = process_form_data("movie-selection-form");
    const ticket_count = document.getElementById("ticket-selection").value;

    if (movie_id !== null) {
        const url = BACKEND_URL + '/movies/selection' + '?id=' + movie_id;
        const response = await fetch(url,  {
            method: 'POST'
        });
        if (response.status !== 200) {
            // display error
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
