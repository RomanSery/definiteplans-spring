const cacheName = 'cache-ld-v1';
const precacheResources = [
    '/',
    '/img/android-chrome-192x192.png',
    '/img/android-chrome-512x512.png',
    '/img/apple-touch-icon.png',
    '/img/favicon.ico',
    '/img/favicon-16x16.ico',
    '/img/favicon-32x32.ico',
    '/img/female.jpg',
    '/img/male.jpg',
    '/img/question_mark.jpg',
    '/img/zoom.png'
];

self.addEventListener('install', event => {
    console.log('Service worker install event!');
    event.waitUntil(
        caches.open(cacheName)
            .then(cache => {
                return cache.addAll(precacheResources);
            })
    );
});

self.addEventListener('activate', event => {
    console.log('Service worker activate event!');
});

self.addEventListener('fetch', event => {
    console.log('Fetch intercepted for:', event.request.url);
    event.respondWith(caches.match(event.request)
        .then(cachedResponse => {
            if (cachedResponse) {
                return cachedResponse;
            }
            return fetch(event.request);
        })
    );
});