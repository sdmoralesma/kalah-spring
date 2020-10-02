const cacheName = 'stockz-cache-v0.0.5';
const resources = ['index.html', 'style.css', 'app.js', 'd3/d3.js', 'images/logo.svg'];

const prefetch = (name) => caches.open(name).then(cache => cache.addAll(resources))

self.addEventListener('install', event => {
  self.skipWaiting();
  event.waitUntil(prefetch(cacheName))
});

self.addEventListener('fetch', event => {
  const {request} = event
  event.respondWith(
      caches.match(request).then(response => (response || fetch(request))));
});

self.addEventListener('activate', event => {
  self.clients.claim();
  const staleCaches = caches.keys()
  .then(keys => keys.filter(k => k !== cacheName))
  .map(stale => caches.delete(stale));
  event.waitUntil(staleCaches);
});

self.addEventListener('message', event => {
  caches.delete(cacheName).then(_ => prefetch(cacheName))
});