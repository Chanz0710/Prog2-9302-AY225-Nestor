// GAME ACCOUNTS
const accounts = {
    traveler: "traveler123",
    knight: "knight123",
    mage: "mage123",
    goblin: "goblin123",
    guardian: "guardian123"
};

const avatars = {
    traveler: "🧭",
    knight: "🗡️",
    mage: "🪄",
    goblin: "👹",
    guardian: "🛡️"
};

// SOUNDS
const successSound = new Audio("https://actions.google.com/sounds/v1/cartoon/clang.ogg");
const failSound = new Audio("https://actions.google.com/sounds/v1/cartoon/wood_plank_flicks.ogg");

// ELEMENTS
const loginSection = document.getElementById("loginSection");
const successSection = document.getElementById("successSection");
const historySection = document.getElementById("historySection");

const loginForm = document.getElementById("loginForm");
const errorBox = document.getElementById("loginError");
const avatar = document.getElementById("pixelCharacter");

const historyBody = document.getElementById("historyBody");
const recordCount = document.getElementById("recordCount");

// LOAD HISTORY SAFELY
let history = JSON.parse(localStorage.getItem("attendance")) || [];
history = history.map((r, i) => ({
    no: i + 1,
    user: r.user || r.username || "unknown",
    time: r.time || r.timeIn || "N/A"
}));
saveHistory();

// LOGIN
loginForm.addEventListener("submit", e => {
    e.preventDefault();

    const user = username.value.trim();
    const pass = password.value;

    if (!accounts[user] || accounts[user] !== pass) {
        errorBox.textContent = "❌ QUEST FAILED!";
        errorBox.style.display = "block";
        failSound.play();
        return;
    }

    errorBox.style.display = "none";

    avatar.textContent = avatars[user];
    avatar.classList.add("jump");
    setTimeout(() => avatar.classList.remove("jump"), 400);

    successSound.play();
    recordAttendance(user);

    successMessage.textContent = `${user.toUpperCase()} ENTERED THE REALM`;

    loginSection.classList.add("hidden");
    successSection.classList.remove("hidden");
});

// RECORD
function recordAttendance(user) {
    history.push({
        no: history.length + 1,
        user,
        time: new Date().toLocaleString()
    });
    saveHistory();
}

function saveHistory() {
    localStorage.setItem("attendance", JSON.stringify(history));
}

// HISTORY VIEW
document.getElementById("historyBtn").onclick = () => {
    successSection.classList.add("hidden");
    historySection.classList.remove("hidden");

    historyBody.innerHTML = "";
    history.forEach(r => {
        historyBody.innerHTML += `
            <tr>
                <td>${r.no}</td>
                <td>${r.user}</td>
                <td>${r.time}</td>
            </tr>
        `;
    });

    recordCount.textContent = `Total Records: ${history.length}`;
};

// BACK
document.getElementById("backBtn").onclick = resetToLogin;
document.getElementById("backFromHistoryBtn").onclick = resetToLogin;

function resetToLogin() {
    username.value = "";
    password.value = "";
    avatar.textContent = "🧭";
    errorBox.style.display = "none";

    successSection.classList.add("hidden");
    historySection.classList.add("hidden");
    loginSection.classList.remove("hidden");
}

// CSV
document.getElementById("exportBtn").onclick = () => {
    let csv = "No,Character,Time In\n";
    history.forEach(r => {
        csv += `${r.no},${r.user},${r.time}\n`;
    });

    const blob = new Blob([csv], { type: "text/csv" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "attendance_quest.csv";
    link.click();
};

// CLEAR
document.getElementById("clearBtn").onclick = () => {
    if (!confirm("Erase quest log?")) return;
    history = [];
    localStorage.removeItem("attendance");
    historyBody.innerHTML = "";
    recordCount.textContent = "Total Records: 0";
};
