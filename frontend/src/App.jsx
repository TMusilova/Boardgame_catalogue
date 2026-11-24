import { Routes, Route } from 'react-router-dom'

import Home from './components/Home.jsx'
import Details from './components/Detail.jsx'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/games/new" element={<Details isNew/>} />
      <Route path="/games/:id" element={<Details />} />
    </Routes>
  )
}

export default App
