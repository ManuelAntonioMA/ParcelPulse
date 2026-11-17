import { useState } from 'react';
import api from './api';
import './App.css';

function App() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [username, setUsername] = useState('');
  const [product, setProduct] = useState(null);
  const [message, setMessage] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/login', { username });
      const jwt = response.data.token;
      localStorage.setItem('token', jwt);
      setToken(jwt);
      setMessage(`Welcome back, ${username}!`);
    } catch (error) {
      setMessage('Login failed.');
    }
  };

  const handleFetchProduct = async () => {
    try {
      // Fetching the MacBook Pro we inserted via Flyway
      const response = await api.get('/products/1');
      setProduct(response.data);
      setMessage('Product fetched successfully! Check backend logs for cache hits.');
    } catch (error) {
      setMessage('Failed to fetch product. Are you logged in?');
    }
  };

  const handlePurchase = async () => {
    try {
      const order = {
        customerId: "web_user_1",
        items: [{ productId: product.id, quantity: 1 }]
      };
      await api.post('/orders', order);
      setMessage(`Successfully purchased 1 ${product.name}!`);
    } catch (error) {
      // Catches our custom Optimistic Locking 400 error!
      setMessage(error.response?.data?.error || 'Purchase failed.');
    }
  };

  if (!token) {
    return (
      <div className="card">
        <h2>ParcelPulse Employee Login</h2>
        <form onSubmit={handleLogin}>
          <input 
            type="text" 
            placeholder="Enter username" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
          />
          <button type="submit">Sign In</button>
        </form>
      </div>
    );
  }

  return (
    <div className="card">
      <h2>ParcelPulse Storefront</h2>
      <p style={{ color: 'aquamarine' }}>{message}</p>
      
      <div style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginBottom: '20px' }}>
        <button onClick={handleFetchProduct}>Load Catalog</button>
        <button onClick={() => { localStorage.removeItem('token'); setToken(null); }}>Log Out</button>
      </div>

      {product && (
        <div style={{ border: '1px solid #444', padding: '20px', borderRadius: '8px' }}>
          <h3>{product.name}</h3>
          <p>Price: ${product.price}</p>
          <button onClick={handlePurchase} style={{ backgroundColor: '#646cff' }}>
            Buy Now
          </button>
        </div>
      )}
    </div>
  );
}

export default App;