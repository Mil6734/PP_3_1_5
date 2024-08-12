
function loadUsers() {
    fetch('/api/admin/users')
        .then(response => response.json())
        .then(users => {
            const tableBody = document.getElementById('tableUsers');
            tableBody.innerHTML = ''; // Clear previous content
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.username}</td>
                    <td>${user.roles.map(role => role.name).join(', ')}</td>
                    <td><button class="btn btn-info btn-sm" onclick="editUser(${user.id})">Edit</button></td>
                    <td><button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Delete</button></td>
                `;
                tableBody.appendChild(row);
            });
        });
}


function editUser(id) {
    fetch(`/api/admin/users/${id}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('editId').value = user.id;
            document.getElementById('editUserName').value = user.username;
            document.getElementById('editName').value = user.name;
            document.getElementById('editLastName').value = user.lastName;
            document.getElementById('editAge').value = user.age;
            document.getElementById('editPassword').value = '';
            const rolesEdit = document.getElementById('rolesEdit');
            Array.from(rolesEdit.options).forEach(option => {
                option.selected = user.roles.some(role => role.id == option.value);
            });
            $('#editModal').modal('show');
        });
}

document.getElementById('editUserForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const id = document.getElementById('editId').value;
    const user = {
        username: document.getElementById('editUserName').value,
        name: document.getElementById('editName').value,
        lastName: document.getElementById('editLastName').value,
        age: document.getElementById('editAge').value
    };

    const rolesEdit = document.getElementById('rolesEdit');
    const selectedRoles = Array.from(rolesEdit.selectedOptions);
    const roleIds = selectedRoles.map(option => option.value);

    const queryParams = new URLSearchParams();
    if (roleIds.length > 0) {
        queryParams.append('roleId', roleIds.join(','));
    }

    const newPassword = document.getElementById('editPassword').value.trim();
    if (newPassword) {
        user.password = newPassword;
    }

    fetch(`/api/admin/users/${id}?${queryParams.toString()}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(response => {
        if (response.ok) {
            loadUsers();
            $('#editModal').modal('hide');
        } else {
            console.log("Failed request: ", response);
            alert('Failed to update user.');
        }
    });
});


function deleteUser(id) {
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`/api/admin/users/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    loadUsers();
                }
            });
    }
}


document.getElementById('newUserForm').addEventListener('submit', function (event) {
    event.preventDefault();


    const rolesNew = document.getElementById('rolesNew');
    const selectedRoles = Array.from(rolesNew.selectedOptions);
    if (selectedRoles.length === 0) {
        rolesNew.classList.add('is-invalid');
        return;
    }
    rolesNew.classList.remove('is-invalid');


    const user = {
        username: document.getElementById('newUserName').value,
        name: document.getElementById('newName').value,
        lastName: document.getElementById('newLastName').value,
        age: document.getElementById('newAge').value,
        password: document.getElementById('newPassword').value
    };


    const roleIds = selectedRoles.map(option => option.value);


    fetch(`/api/admin/users?roleId=${roleIds.join(',')}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.ok) {
                loadUsers();
                $('#newUserModal').modal('hide');
            }
        });
});

loadUsers();