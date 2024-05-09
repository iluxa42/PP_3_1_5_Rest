const URL = 'http://localhost:8080/api/users/';

document.addEventListener('DOMContentLoaded', () => {
    getCurrentAuthUser();

    // активировать User button и panel, если вход с ролью User`a
    if (document.getElementById('admin-tab-btn') == null) {
        document.title = "User page";
        document.getElementById('user-tab-btn').click();
        return;
    }

    // добавить roles в формы
    fetch(URL + 'roles')
        .then(response => response.json())
        .then(roles => {
            console.log('добавить roles в формы');
            let options = '';
            roles.forEach(role =>
                options += `<option value="${role.id}">${role.name.replace('ROLE_', '')}</option>`
            );

            document.getElementById('new-select-roles').innerHTML = options;
            document.getElementById('modal-select-roles').innerHTML = options;
        })

    printTableOfAllUsers();
}, true);


function getCurrentAuthUser() {
    fetch(URL + 'currentAuth')
        .then(response => response.json())
        .then(user => {
            let rolesString = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');

            // Table current user info
            document.getElementById('auth-user-table').innerHTML =
                                   `<tr>
                                        <td>${user.id}</td>
                                        <td>${user.name}</td>
                                        <td>${user.surname}</td>
                                        <td>${user.age}</td>
                                        <td>${user.salary}</td>                            
                                        <td>${user.email}</td>
                                        <td>${user.username}</td>
                                        <td>${rolesString}</td>
                                    </tr>`;

            // navbar current user info
            document.getElementById('navbar-auth-user-info').innerHTML =
                `<strong>${user.email}</strong> with roles: <span>${rolesString}</span>`;
        });
}


function printTableOfAllUsers() {
    fetch(URL)
        .then(response => response.json())
        .then(users => {
            let rows = '';

            for (let user of users) {
                rows += `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.surname}</td>
                            <td>${user.age}</td>
                            <td>${user.salary}</td>                            
                            <td>${user.email}</td>
                            <td>${user.username}</td>
                            <td>${user.roles.map(role => role.name.replace('ROLE_', '')).join(' ')}</td>
                            <td>
                                <button type="button" class="btn btn-info btn-sm"
                                        data-bs-toggle="modal" data-bs-target="#modal"
                                        onclick="fillEditModal(${user.id})">
                                    Edit
                                </button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger btn-sm"
                                        data-bs-toggle="modal" data-bs-target="#modal"
                                        onclick="fillDeleteModal(${user.id})">
                                    Delete
                                </button>
                            </td>
                        </tr>`;
            }

            document.getElementById('usersTableBody').innerHTML = rows;
        })
}


function fillEditModal(userId) { fillModal(userId, false) }
function fillDeleteModal(userId) { fillModal(userId, true) }

function fillModal(userId, isDelete) {
    fetch(URL + userId)
        .then(response => response.json())
        .then(user => {
            document.getElementById('modal-title').innerText
                = (isDelete) ? 'Delete user' : 'Edit user';
            document.getElementById('modal-action-btn').innerText
                = (isDelete) ? 'Delete' : 'Edit';
            document.getElementById('modal-action-btn').classList
                .replace((isDelete) ? 'btn-primary' : 'btn-danger',
                    (isDelete) ? 'btn-danger' : 'btn-primary');

            const idValuesArr = [
                ['name', user.name],
                ['surname', user.surname],
                ['age', user.age],
                ['salary', user.salary],
                ['email', user.email],
                ['username', user.username],
                ['password', user.password]
            ];

            document.getElementById('id').value = user.id;

            for (let pair of idValuesArr) {
                let el = document.getElementById(pair[0]);
                el.value = pair[1];
                el.readOnly = isDelete;
                if (isDelete) {
                    el.classList.add('bg-light');
                } else {
                    el.classList.remove('bg-light');
                }
            }

            let newRolesSel = document.getElementById('modal-select-roles');
            newRolesSel.options[0].selected = user.roles.find(r => r.name === "ROLE_ADMIN");
            newRolesSel.options[1].selected = user.roles.find(r => r.name === "ROLE_USER");
            newRolesSel.disabled = isDelete;

            const modalForm = document.forms['modal-form'];
            const submitBtn = document.getElementById('modal-action-btn');

            submitBtn.onclick = () => {
                fetch((isDelete ? URL + userId : URL), {
                    method: (isDelete ? 'DELETE' : 'PUT'),
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: (isDelete ? '' : JSON.stringify({
                        id: userId,
                        name: modalForm.name.value,
                        surname: modalForm.surname.value,
                        age: modalForm.age.value,
                        salary: modalForm.salary.value,
                        email: modalForm.email.value,
                        username: modalForm.username.value,
                        password: modalForm.password.value,
                        roles: Array.from(modalForm.roles.selectedOptions)
                            .map(option => ({
                                id: +option.value,
                                name: 'ROLE_'.concat(option.text)
                            }))
                    }))
                })
                    .then(() => {
                        modalForm.reset();
                        bootstrap.Modal.getInstance(document.getElementById("modal")).hide();
                        printTableOfAllUsers();
                    });
            };
        });
}


// Add new user
const newUserForm = document.forms['add-new-user-form'];
newUserForm.addEventListener('submit', event => {
    event.preventDefault();

    const newUserData = {
              id: 0,
            name: newUserForm.newName.value,
         surname: newUserForm.newSurname.value,
             age: newUserForm.newAge.value,
          salary: newUserForm.newSalary.value,
           email: newUserForm.newEmail.value,
        username: newUserForm.newUsername.value,
        password: newUserForm.newPassword.value,
           roles: Array.from(newUserForm.roles.selectedOptions)
                       .map(option => ({
                           id: +option.value,
                           name: 'ROLE_'.concat(option.text)
                       }))
    }

    fetch(URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUserData)
    })
        .then(() => {
            newUserForm.reset();
            document.getElementById('users-table-tab').click();
            printTableOfAllUsers();
        })
});