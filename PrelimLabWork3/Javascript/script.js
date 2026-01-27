// ============================================
// CALCULATION FUNCTION
// ============================================

function calculateGrade() {
    // Hide previous results and errors
    document.getElementById('outputSection').classList.remove('visible');
    document.getElementById('errorMessage').classList.remove('visible');
    
    // Get input values
    const attendance = parseFloat(document.getElementById('attendance').value);
    const lab1 = parseFloat(document.getElementById('lab1').value);
    const lab2 = parseFloat(document.getElementById('lab2').value);
    const lab3 = parseFloat(document.getElementById('lab3').value);
    
    // Validate inputs
    if (isNaN(attendance) || isNaN(lab1) || isNaN(lab2) || isNaN(lab3)) {
        showError('⚠️ Please enter valid numeric values in all fields.');
        return;
    }
    
    if (attendance < 0) {
        showError('⚠️ Attendance must be a non-negative number.');
        return;
    }
    
    if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
        showError('⚠️ Lab grades must be between 0 and 100.');
        return;
    }
    
    // Calculate Lab Work Average
    const labWorkAverage = (lab1 + lab2 + lab3) / 3;
    
    // Calculate Class Standing
    const classStanding = (attendance * 0.40) + (labWorkAverage * 0.60);
    
    // Calculate required Prelim Exam scores
    // Corrected formula: Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
    const requiredForPass = (75 - (classStanding * 0.70)) / 0.30;
    const requiredForExcellent = (100 - (classStanding * 0.70)) / 0.30;
    
    // Display input values with animation
    animateValue('displayAttendance', attendance.toFixed(2));
    animateValue('displayLab1', lab1.toFixed(2));
    animateValue('displayLab2', lab2.toFixed(2));
    animateValue('displayLab3', lab3.toFixed(2));
    
    // Display computed values with animation
    animateValue('displayLabAvg', labWorkAverage.toFixed(2));
    animateValue('displayClassStanding', classStanding.toFixed(2));
    
    // Display required scores
    displayRequiredScore('displayPassScore', requiredForPass);
    displayRequiredScore('displayExcellentScore', requiredForExcellent);
    
    // Generate remarks
    generateRemarks(requiredForPass, requiredForExcellent, classStanding);
    
    // Show output section with animation
    setTimeout(() => {
        document.getElementById('outputSection').classList.add('visible');
        scrollToResults();
    }, 100);
}

// ============================================
// HELPER FUNCTIONS
// ============================================

function displayRequiredScore(elementId, score) {
    const element = document.getElementById(elementId);
    
    if (score <= 0) {
        element.textContent = '✅ Already achieved';
        element.style.color = '#28a745';
    } else if (score > 100) {
        element.textContent = '❌ Impossible';
        element.style.color = '#dc3545';
    } else {
        animateValue(elementId, score.toFixed(2));
        element.style.color = '#333';
    }
}

function animateValue(elementId, finalValue) {
    const element = document.getElementById(elementId);
    const duration = 1000; // 1 second
    const steps = 60;
    const stepDuration = duration / steps;
    
    // Try to parse as number for animation, otherwise just set text
    const numericValue = parseFloat(finalValue);
    
    if (isNaN(numericValue)) {
        element.textContent = finalValue;
        return;
    }
    
    let currentStep = 0;
    const increment = numericValue / steps;
    let currentValue = 0;
    
    const timer = setInterval(() => {
        currentStep++;
        currentValue += increment;
        
        if (currentStep >= steps) {
            element.textContent = finalValue;
            clearInterval(timer);
        } else {
            element.textContent = currentValue.toFixed(2);
        }
    }, stepDuration);
}

function generateRemarks(requiredForPass, requiredForExcellent, classStanding) {
    const remarksSection = document.getElementById('remarksSection');
    let remarksHTML = '';
    let remarksClass = 'remarks-section ';
    
    // Determine overall status
    if (requiredForPass <= 0) {
        remarksClass += 'remarks-success';
        remarksHTML = `
            <p><strong>🎉 Outstanding Achievement!</strong></p>
            <p>Congratulations! You have already secured a passing grade based on your Class Standing of <strong>${classStanding.toFixed(2)}</strong>.</p>
            <p>Even with a score of 0 on the Prelim Exam, you will pass the Prelim period! Keep up the excellent work! 🌟</p>
        `;
    } else if (requiredForPass > 100) {
        remarksClass += 'remarks-danger';
        const maxGrade = (100 * 0.30) + (classStanding * 0.70);
        remarksHTML = `
            <p><strong>⚠️ Critical Notice</strong></p>
            <p>Unfortunately, it is mathematically impossible to achieve a passing grade of 75 based on your current Class Standing of <strong>${classStanding.toFixed(2)}</strong>.</p>
            <p>Maximum possible Prelim Grade (with perfect exam score): <strong>${maxGrade.toFixed(2)}</strong></p>
            <p>💡 <em>Recommendation:</em> Focus on improving your attendance and lab work grades for the next grading period.</p>
        `;
    } else {
        remarksClass += 'remarks-warning';
        remarksHTML = `
            <p><strong>📌 Your Target</strong></p>
            <p>You need to score <strong>${requiredForPass.toFixed(2)}</strong> or higher on the Prelim Exam to pass the Prelim period.</p>
        `;
    }
    
    // Add information about excellent grade
    if (requiredForExcellent <= 0) {
        remarksHTML += `<p><strong>⭐ Bonus:</strong> You have already achieved an excellent standing! Maintain this performance! 🏆</p>`;
    } else if (requiredForExcellent > 100) {
        if (requiredForPass <= 100) {
            remarksHTML += `<p><strong>ℹ️ Note:</strong> Achieving an excellent grade (100) is not mathematically possible, but you can still pass!</p>`;
        }
    } else if (requiredForExcellent <= 100 && requiredForPass <= 100) {
        remarksHTML += `<p><strong>🎯 Stretch Goal:</strong> To achieve excellent standing (100), you need to score <strong>${requiredForExcellent.toFixed(2)}</strong> on the Prelim Exam.</p>`;
    }
    
    remarksSection.className = remarksClass;
    remarksSection.innerHTML = remarksHTML;
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    errorDiv.textContent = message;
    errorDiv.classList.add('visible');
    
    // Shake animation
    errorDiv.style.animation = 'none';
    setTimeout(() => {
        errorDiv.style.animation = 'shake 0.5s ease';
    }, 10);
}

function clearFields() {
    // Clear input fields with animation
    const fields = ['attendance', 'lab1', 'lab2', 'lab3'];
    fields.forEach((field, index) => {
        setTimeout(() => {
            const element = document.getElementById(field);
            element.value = '';
            element.style.animation = 'none';
            setTimeout(() => {
                element.style.animation = 'slideIn 0.3s ease-out';
            }, 10);
        }, index * 50);
    });
    
    // Hide results and errors
    document.getElementById('outputSection').classList.remove('visible');
    document.getElementById('errorMessage').classList.remove('visible');
    
    // Reset display values
    const displayElements = [
        'displayAttendance', 'displayLab1', 'displayLab2', 'displayLab3',
        'displayLabAvg', 'displayClassStanding', 'displayPassScore', 'displayExcellentScore'
    ];
    
    displayElements.forEach(id => {
        document.getElementById(id).textContent = '--';
        document.getElementById(id).style.color = '#333';
    });
    
    // Focus on first field
    setTimeout(() => {
        document.getElementById('attendance').focus();
    }, 300);
}

function scrollToResults() {
    const resultsCard = document.getElementById('outputSection');
    resultsCard.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

// ============================================
// EVENT LISTENERS
// ============================================

// Add Enter key support for all input fields
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input[type="number"]');
    
    inputs.forEach(input => {
        input.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                calculateGrade();
            }
        });
        
        // Add focus animation
        input.addEventListener('focus', function() {
            this.style.transform = 'translateY(-2px)';
        });
        
        input.addEventListener('blur', function() {
            this.style.transform = 'translateY(0)';
        });
    });
    
    // Create floating particles
    createParticles();
});

// ============================================
// PARTICLE ANIMATION
// ============================================

function createParticles() {
    const particlesContainer = document.getElementById('particles');
    const particleCount = 20;
    
    for (let i = 0; i < particleCount; i++) {
        const particle = document.createElement('div');
        particle.style.position = 'absolute';
        particle.style.width = Math.random() * 4 + 2 + 'px';
        particle.style.height = particle.style.width;
        particle.style.background = 'rgba(255, 255, 255, 0.5)';
        particle.style.borderRadius = '50%';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.top = Math.random() * 100 + '%';
        particle.style.animation = `floatParticle ${Math.random() * 10 + 10}s ease-in-out infinite`;
        particle.style.animationDelay = Math.random() * 5 + 's';
        
        particlesContainer.appendChild(particle);
    }
}

// Add particle animation keyframes dynamically
const style = document.createElement('style');
style.textContent = `
    @keyframes floatParticle {
        0%, 100% {
            transform: translate(0, 0);
            opacity: 0;
        }
        10% {
            opacity: 1;
        }
        90% {
            opacity: 1;
        }
        50% {
            transform: translate(${Math.random() * 200 - 100}px, ${Math.random() * 200 - 100}px);
        }
    }
`;
document.head.appendChild(style);