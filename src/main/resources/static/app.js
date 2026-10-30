// app.js - Simple front-end for Product Management API

const API_BASE = '/api/products'; // REST endpoints under /api/products

document.addEventListener('DOMContentLoaded', () => {
  loadProducts();
  const form = document.getElementById('product-form');
  form.addEventListener('submit', onSubmit);
  const tbody = document.getElementById('products-body');
  tbody.addEventListener('click', onTableAction);
});

function onSubmit(event) {
  event.preventDefault();
  const id = document.getElementById('product-id').value;
  const name = document.getElementById('name').value.trim();
  const price = Number(document.getElementById('price').value);
  if (!name || isNaN(price)) return alert('Please fill all fields');

  const payload = { name, price, stock: 0, description: '' };
  const method = id ? 'PUT' : 'POST';
  const url = id ? `${API_BASE}/${id}` : API_BASE;

  fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
    .then(handleResponse)
    .then(() => {
      clearForm();
      loadProducts();
    })
    .catch(err => console.error(err));
}

function loadProducts() {
  fetch(API_BASE)
    .then(handleResponse)
    .then(renderProducts)
    .catch(err => console.error('Failed to load products', err));
}

function renderProducts(products) {
  const tbody = document.getElementById('products-body');
  tbody.innerHTML = '';
  products.forEach(p => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${p.id}</td>
      <td>${p.name}</td>
      <td>${p.price}</td>
      <td>
        <button class="action-btn" data-id="${p.id}" data-action="edit">✎</button>
        <button class="action-btn" data-id="${p.id}" data-action="delete">🗑️</button>
      </td>`;
    tbody.appendChild(tr);
  });
}

function onTableAction(event) {
  const target = event.target;
  if (target.tagName !== 'BUTTON') return;
  const id = target.dataset.id;
  const action = target.dataset.action;
  if (action === 'delete') {
    if (!confirm('Delete this product?')) return;
    fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
      .then(handleResponse)
      .then(loadProducts)
      .catch(err => console.error(err));
  } else if (action === 'edit') {
    fetch(`${API_BASE}/${id}`)
      .then(handleResponse)
      .then(product => {
        document.getElementById('product-id').value = product.id;
        document.getElementById('name').value = product.name;
        document.getElementById('price').value = product.price;
        document.getElementById('submit-btn').textContent = '更新';
      })
      .catch(err => console.error(err));
  }
}

function clearForm() {
  document.getElementById('product-id').value = '';
  document.getElementById('name').value = '';
  document.getElementById('price').value = '';
  document.getElementById('submit-btn').textContent = '追加';
}

function handleResponse(response) {
  if (!response.ok) {
    return response.text().then(text => { throw new Error(text || response.statusText); });
  }
  if (response.status === 204) {
    return null;
  }
  return response.json();
}
