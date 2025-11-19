document.addEventListener('DOMContentLoaded', () => {
    console.log("Halaman dimuat. Memulai script notifikasi.");

    if (!('Notification' in window)) {
        console.error("Browser ini tidak mendukung Notifikasi Desktop.");
        return;
    }

    if (Notification.permission !== 'granted') {
        console.log("Meminta izin notifikasi...");
        Notification.requestPermission().then(permission => {
            console.log(`Izin notifikasi: ${permission}`);
        });
    } else {
        console.log("Izin notifikasi sudah diberikan.");
    }

    function showNotification(title, body) {
        console.log(`Mencoba menampilkan notifikasi: Title=${title}, Body=${body}`);
        if (Notification.permission === 'granted') {
            new Notification(title, { body: body, icon: '/favicon.svg' });
            console.log("Objek notifikasi berhasil dibuat.");
        } else {
            console.warn("Tidak bisa menampilkan notifikasi karena izin ditolak.");
        }
    }

    console.log("Menghubungkan ke saluran notifikasi global di /notifications...");
    const eventSource = new EventSource('/notifications');

    eventSource.onopen = function() {
        console.log("Koneksi ke /notifications BERHASIL dibuka.");
    };

    eventSource.addEventListener('jobCompletion', function(event) {
        console.log("Menerima event 'jobCompletion' dari server.");
        console.log("Data mentah:", event.data);
        const job = JSON.parse(event.data);
        if (job.status === 'COMPLETED') {
            showNotification('Report Ready!', `Report for ID ${job.id} is now available.`);
        } else if (job.status === 'FAILED') {
            showNotification('Report Failed', `Report generation failed for ID: ${job.id}`);
        }
    });

    eventSource.onerror = function(err) {
        console.error("Koneksi SSE Error:", err);
    };
});
