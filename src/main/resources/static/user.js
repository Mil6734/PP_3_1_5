function loadUserInfo() {
    fetch('/api/user/current') // Updated endpoint
        .then(response => response.json())
        .then(user => {
            const tableBody = document.getElementById('userInfo');
            tableBody.innerHTML = '';

            const rolesUser = user.roles.map(role => role.name.replace('ROLE_', '')).join(', ');

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.username}</td>
                <td>${rolesUser}</td>
            `;
            tableBody.appendChild(row);
        });
}

document.addEventListener('DOMContentLoaded', loadUserInfo);