const BACKEND_URL = 'http://localhost:8080';

async function load_movie_data() {

    const movies_selection_field = document.getElementById('movie-selection');
    const url = BACKEND_URL + '/movies';
    const response = await fetch(url, {
        method: 'GET',
    });

    if (response.status !== 200) {
        movies_selection_field.innerText = JSON.stringify(response.status);
    } else {
        const movies_selection_field = document.getElementById('movie-selection');
        // const data = await response.json();
        movies_selection_field.innerText = response.toString();
    }
}
