import { useEffect, useState } from "react"
import { Row, Col } from 'reactstrap'
import { useParams, useNavigate } from "react-router-dom"
import { validateGame } from "../gameValidation"

function Details({ isNew = false }) {
  const { id } = useParams()
  const navigate = useNavigate()

  const [game, setGame] = useState(null)
  const [genres, setGenres] = useState([])
  const [error, setError] = useState("")

  // fetch genres and an existing game or set new blank game
  useEffect(() => {
    if (isNew) {
      setGame({
        id: null,
        name: "",
        price: null,
        publisher: "",
        year: "",
        description: "",
        genre_id: null
      })
    } else {
      fetch(`/api/games/${id}`)
        .then(response => response.json())
        .then(data => setGame(data))
        .catch(error => console.error("Error loading game: ", error))
    }

    fetch("/api/genres")
      .then(response => response.json())
      .then(data => setGenres(data))
      .catch(error => console.error("Error loading genres: ", error))
  }, [id])

  // save new game or changes
  function saveGame(event) {
    event.preventDefault()
    setError("")

    const validationError = validateGame(game, isNew)
    if (validationError) {
      setError(validationError)
      return
    }

    const url = isNew ? "/api/games" : `/api/games/${id}`
    const method = isNew ? "POST" : "PUT"

    fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(game)
    })
      .then(async response => {
        const message = await response.text()

        if (!response.ok) {
          setError(message || "Error saving game.")
          return
        }

        navigate("/")
      })
      .catch(error => setError("Server error: " + error.message))
  }

  // delete existing game
  function deleteGame() {
    if (!window.confirm("Are you sure you want to delete this game?")) {
      return
    }

    setError("")

    fetch(`/api/games/${id}`, {
      method: "DELETE"
    })
      .then(async response => {
        const message = await response.text()

        if (!response.ok) {
          setError(message || "Error deleting game.")
          return
        }

        navigate("/")
      })
      .catch(error => setError("Server error: " + error.message))
  }

  // game not loaded yet
  if (game === null) {
    return <p>Loading...</p>
  }

  return (
    <main className="container">
      <h1 className="text-center">{isNew ? "New Game" : "Game Details"}</h1>

      <form className="container-form" onSubmit={saveGame}>
        {error && (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        )}

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mt-3">ID:</label>
          </Col>
          <Col sm="9">
            <input
              type="number"
              className="form-control"
              style={!isNew ? { backgroundColor: "#f5f6fa" } : null}
              value={game.id ?? ""}
              disabled={!isNew}
              required={isNew}
              min={1}
              step={1}
              onChange={event => setGame({
                ...game,
                id: event.target.value === "" ? null : Number(event.target.value)
              })}
            />
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mb-0">Title:</label>
          </Col>
          <Col sm="9">
            <input
              className="form-control"
              value={game.name}
              required
              minLength={2}
              maxLength={500}
              onChange={event => setGame({
                ...game,
                name: event.target.value
              })}
            />
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mt-3">Publisher:</label>
          </Col>
          <Col sm="9">
            <input
              className="form-control"
              value={game.publisher}
              onChange={event => setGame({
                ...game,
                publisher: event.target.value
              })}
            />
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col>
            <label className="form-label mt-3">Publication year:</label>
          </Col>
          <Col>
            <input
              type="number"
              className="form-control"
              value={game.year ?? ""}
              min={1}
              max={new Date().getFullYear()}
              step={1}
              onChange={event => setGame({
                ...game,
                year: event.target.value === "" ? null : Number(event.target.value)
              })}
            />
          </Col>

          <Col>
            <label className="form-label d-block text-end mt-3 me-3">Price:</label>
          </Col>
          <Col>
            <input
              type="number"
              className="form-control"
              value={game.price ?? ""}
              min={0}
              onChange={event => setGame({
                ...game,
                price: event.target.value === "" ? null : Number(event.target.value)
              })}
            />
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mt-3">Description:</label>
          </Col>
          <Col sm="9">
            <textarea
              className="form-control"
              style={{ fieldSizing: "content" }}
              value={game.description}
              required
              minLength={3}
              maxLength={5000}
              onChange={(event) => setGame({
                ...game,
                description: event.target.value,
              })}
            />
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mt-3">Genre:</label>
          </Col>
          <Col sm="9">
            <select
              className="form-select"
              value={game.genre_id ?? ""}
              required
              onChange={event => setGame({
                ...game,
                genre_id: event.target.value === "" ? null : Number(event.target.value)
              })}
            >
              <option value="">Select genre</option>
              {genres.map(genre => (
                <option key={genre.id} value={genre.id}>
                  {genre.name}
                </option>
              ))}
            </select>
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mt-2">Genre ID:</label>
          </Col>
          <Col sm="9">
            <p className="form-label m-0" style={{ paddingLeft: "0.75rem" }}>
              {genres.find(genre => genre.id === game.genre_id)?.id}
            </p>
          </Col>
        </Row>

        <Row className="mb-1 align-items-center">
          <Col sm="3">
            <label className="form-label mt-2">Genre description:</label>
          </Col>
          <Col sm="9">
            <p className="form-label m-0" style={{ paddingLeft: "0.75rem" }}>
              {genres.find(genre => genre.id === game.genre_id)?.description}
            </p>
          </Col>
        </Row>

        <div className="d-flex justify-content-end gap-2 mt-4">
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => navigate("/")}
          >
            Cancel
          </button>

          {!isNew && (
            <button
              type="button"
              className="btn btn-danger"
              onClick={deleteGame}
            >
              Delete
            </button>
          )}

          <button type="submit" className="btn buttons">Save</button>
        </div>
      </form>
    </main>
  )
}

export default Details