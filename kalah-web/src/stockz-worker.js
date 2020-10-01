const cacheName = 'stockz-cache';
const resources = ['index.html', 'style.css', 'configuration.json', 'app.js',
  'AirNav.js', "AirSlot.js", "views/AboutView.js"];

self.addEventListener('install', event =>
    event.waitUntil(
        caches.open(cacheName).then(cache => cache.addAll(resources)))
);

self.addEventListener('fetch', event => {
  const {request} = event
  console.log('on-fetch', request);
  event.respondWith(
      caches.match(request).then(response => (response || fetch (request))));
});