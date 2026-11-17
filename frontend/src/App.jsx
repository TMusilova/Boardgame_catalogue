import { useEffect, useState } from 'react'
import './styles/App.css'

function App() {
  const [games, setGames] = useState([])
  const [genres, setGenres] = useState([])
  const [query, setQuery] = useState('')

  // fetch all games and genres when page is opened
  useEffect(() => {
    fetch("/api/games")
      .then(response => response.json())
      .then(data => setGames(data))
      .catch(error => console.error("Error loading games: ", error))

    fetch("/api/genres")
      .then(response => response.json())
      .then(data => setGenres(data))
      .catch(error => console.error("Error loading genres: ", error))
  }, [])

  // call search endpoint when query was given
  function searchGames(event) {
    event.preventDefault()

    const url = query.trim()
      ? `/api/games/search?query=${encodeURIComponent(query)}`
      : '/api/games'

    fetch(url)
      .then(response => response.json())
      .then(data => setGames(data))
      .catch(error => console.error('Error searching games:', error))
  }

  return (
    <>
      <main className="container">
        <h1 className="text-center mt-4 mb-3">Board Game Catalog</h1>
        <h2 className='text-center fst-italic'>
          Look up your favourite board games or discover new ones.
        </h2>

        <form onSubmit={searchGames} className="search-form d-flex gap-2 mb-4">
          <input
            type="search"
            className="form-control"
            placeholder="Search games by title or description..."
            value={query}
            onChange={event => setQuery(event.target.value)}
          />
          <button type="submit" className="btn buttons">
            Search
          </button>
        </form>

        <div className='games-table'>
          <table className="table table-striped table-hover mb-0">
            <thead>
              <tr>
                <th>Title</th>
                <th>Genre</th>
                <th>Publisher</th>
                <th>Year</th>
                <th>Price</th>
              </tr>
            </thead>
            <tbody>
              {games.length > 0 ? (
                games.map(game => (
                  <tr key={game.id}>
                    <td>{game.name}</td>
                    <td>{genres.find(genre => genre.id === game.genre_id)?.name}</td>
                    <td>{game.publisher}</td>
                    <td>{game.year}</td>
                    <td>{game.price}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td>No games found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </main>
    </>
  )
}

export default App
