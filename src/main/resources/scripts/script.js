//get all users and filling main table
fetch("/users").then((response) => {
    return response.json()
}).then((users) => {
    const table = document.getElementById("usersTable")
    for (const user of users) {
        const row = table.insertRow()
        row.setAttribute('id', "row" + user.id)
        addUser(row, user)
        addButtons(row, user)
    }
})
let auth
//authority filling
fetch("/users/0").then((response) => {
    return response.json()
}).then((user) => {
    processAuth(user)
})

function processAuth(user) {
    //one user table
    const row = document.getElementById("oneUser")
    row.innerHTML = ""
    addUser(row, user)
    //top header
    let header = document.getElementById("auth")
    let stringRoles = user.roles[0].name
    for (let i = 1; i < user.roles.length; i++) {
        stringRoles += ", " + user.roles[i].name
    }
    header.innerText = `${user.username} with roles: ${stringRoles}`
    auth = user
}

//add new user
const saveForm = document.getElementById("saveForm")
saveForm.addEventListener("submit", (event) => {
    const form = new FormData(saveForm)
    event.preventDefault()
    fetch("/users", {
        method: 'POST', headers: {
            'Accept': 'application/json', 'Content-Type': 'application/json'
        }, body: `{
            ${getUserString(form)}
        }`,
    }).then(response => response.json().then(user => {
        const table = document.getElementById("usersTable")
        const row = table.insertRow()
        row.setAttribute('id', "row" + user.id)
        addUser(row, user)
        addButtons(row, user)
        alert(`User with id=${user.id} was added.`)
    }))
})

//show and hide modal
$('#DefaultModal').modal({
    keyboard: true, backdrop: "static", show: false
}).on("show.bs.modal", (event) => {
    let thisModal = $(event.target);
    let userid = thisModal.attr('data-userid');
    let action = thisModal.attr('data-action');
    processUser(thisModal, userid, action)
}).on("hidden.bs.modal", (e) => {
    let thisModal = $(e.target);
    thisModal.find('.modal-title').html('');
    thisModal.find('.modal-body').html('');
    thisModal.find('.modal-footer').html('');
})


//utils
function addUser(row, user) {
    const id = row.insertCell()
    id.innerText = user.id
    const firstName = row.insertCell()
    firstName.innerText = user.firstName
    const lastName = row.insertCell()
    lastName.innerText = user.lastName
    const age = row.insertCell()
    age.innerText = user.age
    const username = row.insertCell()
    username.innerText = user.username
    let rolesText = ""
    for (const role of user.roles) {
        rolesText += role.name + "\n"
    }
    const roles = row.insertCell()
    roles.innerText = rolesText
}

function addButtons(row, user) {
    const editButton = row.insertCell()
    editButton.innerHTML = ` <button type="button" id="tbe${user.id}" data-userid="${user.id}" data-action="Edit" class="btn btn-primary" 
                                data-toggle="modal" data-target="#DefaultModal">Edit</button>`
    const deleteButton = row.insertCell()
    deleteButton.innerHTML = `<button type="button" id="tbd${user.id}" data-userid="${user.id}" data-action="Delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#DefaultModal">Delete</button>`
    const buttone = document.getElementById(`tbe${user.id}`)
    buttone.addEventListener('click', (event) => {
        processModal(event)
    })
    const buttond = document.getElementById(`tbd${user.id}`)
    buttond.addEventListener('click', (event) => {
        processModal(event)
    })
}

function processModal(event) {
    let defaultModal = $('#DefaultModal');

    let targetButton = $(event.target);
    let buttonUserId = targetButton.attr('data-userid');
    let buttonAction = targetButton.attr('data-action');

    defaultModal.attr('data-userid', buttonUserId);
    defaultModal.attr('data-action', buttonAction);
    defaultModal.modal('show');


}

async function processUser(modal, id, action) {
    let user = await fetch(`/users/${id}`).then((response) => {
        return response.json()
    });
    let ro = ""     //readonly for delete
    let buttonStyle = "primary"
    if (action == "Delete") {
        ro = "disabled"
        buttonStyle = "danger"
    }
    modal.find(".modal-title").html(`${action} user`)
    let editButton = `<button type="submit" form="editForm" class="btn btn-${buttonStyle}" id="editButton">${action}</button>`
    let closeButton = `<button type="button" class="btn btn-muted" data-dismiss="modal">Close</button>`
    modal.find(".modal-footer").append(editButton);
    modal.find(".modal-footer").append(closeButton);
    let selectU = ""
    let selectA = ""
    for (const role of user.roles) {
        if (role.name == "ROLE_USER") {
            selectU = `selected="selected"`
        } else {
            selectA = `selected="selected"`
        }
    }
    let bodyForm = `<form id="editForm" class="bg-white text-center form-group">
                            <label class="font-weight-bold" for="edid">ID</label> <br>
                            <input class="col-4 bg-light" name="id" type="text" id="edid"
                                   value="${user.id}" readonly/> <br>
                            <label class="font-weight-bold" for="username">Email</label> <br>
                            <input class="col-4" name="username" type="text" id="edusername"
                                   value="${user.username}" ${ro}/> <br>
                            <label class="font-weight-bold" for="password">Password</label> <br>
                            <input class="col-4" name="password" type="text" id="edpassword" ${ro}/>
                            <br>
                            <label class="font-weight-bold" for="name">Name</label> <br>
                            <input class="col-4" name="name" type="text" id="edname"
                                   value="${user.firstName}" ${ro}/> <br>
                            <label class="font-weight-bold" for="surname">Surname</label> <br>
                            <input class="col-4" name="surname" type="text" id="edsurname"
                                   value="${user.lastName}" ${ro}/> <br>
                            <label class="font-weight-bold" for="age">Age</label> <br>
                            <input class="col-4" name="age" type="text" id="edage"
                                   value="${user.age}" ${ro}/> <br>
                            <label class="font-weight-bold" for="roles">Roles</label> <br>
                            <select class="col-4 custom-select" name="roles" multiple
                                    id="edroles" ${ro}>
                                <option ${selectA}>ROLE_ADMIN</option>
                                <option ${selectU}>ROLE_USER</option>
                            </select>
                    </form>                 
        `;
    modal.find(".modal-body").append(bodyForm);
    $("#editForm").on("submit", (event) => {
        event.preventDefault()
        if (action == "Delete") {
            deleteUser(id)
        } else {
            editUser(id)
        }
        modal.modal('hide');
    })

}

function editUser(id) {
    const form = new FormData(document.getElementById("editForm"))
    const isAuth = auth.id == id    //if edited user is authenticated
    fetch("/users", {
        method: 'PUT', headers: {
            'Accept': 'application/json', 'Content-Type': 'application/json'
        }, body: `{
    "id": ${id},
    ${getUserString(form)}
}`,
    }).then(response => response.json().then(user => {
        let row = document.getElementById(`row${id}`)
        row.innerHTML = ""
        addUser(row, user)
        addButtons(row, user)
        if (isAuth) {
            processAuth(user)
        }
    }))
}

function deleteUser(id) {
    fetch("/users/" + id, {
        method: 'DELETE', headers: {
            'Accept': 'application/json', 'Content-Type': 'application/json'
        },
    }).then(response => response.text().then(text => {
        let row = document.getElementById(`row${id}`)
        row.remove()
        console.log(text)
    }))
}

function getUserString(form) {                 //from form
    const roles = form.getAll("roles")  //get roles as array
    let temp = ""
    for (let i = 0; i < roles.length; i++) {
        let id = 1
        if (roles[i] == "ROLE_USER") {
            id = 2
        }
        temp += `
                {
                "id": ${id},
                "name": "${roles[i]}"
                }`
        if (i != roles.length - 1) {
            temp += ","
        }
    }
    return `"username": "${form.get("username")}",
    "password": "${form.get("password")}",
    "age": ${form.get("age")},
    "firstName": "${form.get("name")}",
    "lastName": "${form.get("surname")}",
    "roles": [${temp}
    ]`
}