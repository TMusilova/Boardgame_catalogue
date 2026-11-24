export function validateGame(game, isNew) {
  if (isNew && (!Number.isInteger(game.id) || game.id <= 0)) {
    return "ID must be a positive integer."
  }

  if (game.name.trim().length < 2) {
    return "Title must contain at least 2 characters."
  }

  if (game.description.trim().length < 3) {
    return "Description must contain at least 3 characters."
  }

  if (game.genre_id === null) {
    return "Please select a genre."
  }

  if (game.price !== null && game.price < 0) {
    return "Price cannot be negative."
  }

  if (game.year !== null &&
      (!Number.isInteger(game.year) || game.year <= 0 ||
       game.year > new Date().getFullYear())) {
    return "Invalid publication year."
  }

  return null
}