document.addEventListener("DOMContentLoaded", function () {
    const modalGroup = document.getElementById("modalGroup");
    const modalNewOwner = document.getElementById("modalChangeOwner")
    const modalTitle = modalGroup.querySelector("h2");
    const btnNewGroup = document.getElementById("btnNewItem");
    const formNewGroup = document.getElementById("formNewGroup");
    const inputGroupName = formNewGroup.querySelector("input[name='name']");
    const groupUsersResult =document.getElementById("groupUsersResult");
    const btnAddSelectedUser = document.getElementById("btnAddSelectedUser");
    let currentGroupId = null;
    let clickInsideModal = false;
    let newOwner = null;

    function assignGroupButtonEvents() {
        document.querySelectorAll(".btnChangeOwner").forEach(button => {
            button.removeEventListener("click", openNewOwnerModal);
            button.addEventListener("click", openNewOwnerModal);
        });

        document.querySelectorAll(".btnMoreOptions").forEach(button => {
            button.removeEventListener("click", handleMoreOptionsClick);
            button.addEventListener("click", handleMoreOptionsClick);
        });

        document.querySelectorAll(".btnLeaveGroup").forEach(button => {
            button.removeEventListener("click", handleLeaveGroup);
            button.addEventListener("click", handleLeaveGroup);
        });

        document.querySelectorAll(".btnDeleteGroup").forEach(button => {
            button.removeEventListener("click", handleDeleteGroup);
            button.addEventListener("click", handleDeleteGroup);
        });

        document.querySelectorAll(".btnManageMembers").forEach(button => {
            button.removeEventListener("click", handleManageMembers);
            button.addEventListener("click", handleManageMembers);
        });

        document.querySelectorAll(".btnEditGroup").forEach(button => {
            button.removeEventListener("click", handleEditGroup);
            button.addEventListener("click", handleEditGroup);
        });

        document.querySelectorAll(".modal").forEach(modal => {
            modal.removeEventListener("mousedown", handleModalMouseDown);
            modal.removeEventListener("mouseup", handleModalMouseUp);
            modal.addEventListener("mousedown", handleModalMouseDown);
            modal.addEventListener("mouseup", handleModalMouseUp);
        });
    }

    function handleMoreOptionsClick(event) {
        currentGroupId = event.currentTarget.dataset.groupid;
        const groupItem = event.currentTarget.closest(".group-item");
        const modal = groupItem.querySelector(".modalOptions");

        if (modal) {
            modal.classList.remove("hidden");
            modal.style.display = "flex";
        }
    }

    function handleLeaveGroup(event) {
        const groupId = event.target.dataset.groupid;
        if (!groupId) {
            console.error("No se ha seleccionado un grupo para salir.");
            return;
        }

        if (confirm("\u00BFEst\u00E1s seguro de que deseas salir de este grupo?")) {
            fetch(`/leave_group/${groupId}`, {
                method: "POST",
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        console.error("Error al salir del grupo");
                    }
                })
                .catch(error => console.error("Error en la petición:", error));
        }
    }

    function handleDeleteGroup(event) {
        const groupId = event.target.dataset.groupid;
        if (!groupId) {
            console.error("No se ha seleccionado un grupo para eliminar.");
            return;
        }

        if (confirm("\u00BFEst\u00E1s seguro de que deseas eliminar este grupo? Esta acci\u00F3n es irreversible.")) {
            fetch(`/delete_group/${groupId}`, {
                method: "POST",
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        alert("Error al eliminar el grupo");
                    }
                })
                .catch(error => console.error("Error en la petición:", error));
        }
    }

    function handleManageMembers(event) {
        const groupId = event.target.dataset.groupid;
        if (!groupId) {
            console.error("No se ha seleccionado un grupo.");
            return;
        }
        window.location.href = `/manage_members/${groupId}`;
    }

    function handleEditGroup(event) {
        currentGroupId = event.currentTarget.dataset.groupid;
        const groupItem = event.currentTarget.closest(".group-item");
        inputGroupName.value = groupItem.querySelector("b").innerText; // Mostrar nombre actual en el input
        modalTitle.innerText = "Cambiar Nombre"; // Cambiar título del modal
        modalGroup.style.display = "flex";

        const modal = groupItem.querySelector(".modalOptions");
        if (modal) {
            modal.classList.add("hidden");
            modal.style.display = "none";
        }
    }

    function openNewGroupModal() {
        currentGroupId = null;
        inputGroupName.value = ""; // Vaciar el input
        modalTitle.innerText = "Nuevo Grupo"; // Restablecer título del modal
        modalGroup.style.display = "flex";
    }

    // Cerrar modales al hacer clic fuera
    function handleModalMouseDown(event) {
        clickInsideModal = !!event.target.closest(".modal-content");
    }

    function handleModalMouseUp(event) {
        if (!clickInsideModal && event.target.classList.contains("modal")) {
            event.target.classList.add("hidden");
            event.target.style.display = "none";
        }
    }


    function openNewOwnerModal(event) {
        currentGroupId = event.target.dataset.groupid;
        document.querySelectorAll(".modalOptions").forEach( modal => {
            modal.classList.add("hidden");
            modal.style.display = "none";
        });
        modalNewOwner.classList.remove("hidden");
        modalNewOwner.style.display = "flex";
        showGroupMembers();
    }

    //mostrar miembros del grupo
    function showGroupMembers() {
        // Obtener los miembros del grupo desde el servidor
        fetch(`/group_members?groupId=${currentGroupId}`)
            .then(res => res.json())
            .then(users => {
                groupUsersResult.innerHTML = "";
                users.forEach(user => {
                    const li = document.createElement('li');
                    const radio = document.createElement("input");
                    radio.type = "radio";
                    radio.value = user.id;
                    radio.id = `user-${user.id}`;

                    const label = document.createElement("label");
                    label.textContent = user.name;
                    label.setAttribute("for", `user-${user.id}`);
                    label.style.marginLeft = "8px";

                    radio.addEventListener("change", function ()    {
                        if (radio.checked) {
                            newOwner = user.id;
                        } else {
                            newOwner = null;
                        }
                    });

                    li.appendChild(radio);
                    li.appendChild(label);
                    groupUsersResult.appendChild(li);
                });
            })
            .catch(err => {
                console.error("Error al cargar los miembros:", err);
            });
    }

    // Cambiar propietario
    function handleChangeOwner() {
        if (newOwner == null) {
            alert("No hay ningún usuario seleccionado");
            return;
        }

        fetch(`/group/${currentGroupId}/change_owner?newOwnerId=${newOwner}`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    modalNewOwner.classList.add("hidden");
                    modalNewOwner.style.display = "none";
                    location.reload();
                    alert("Propietario cambiado correctamente")
                } else {
                    alert("Error al cambiar de propietario");
                }
            })
            .catch(error => console.error("Error en la petición:", error));

    }

    // Guardar grupo (crear o editar)
    function saveGroup(event) {
        event.preventDefault();

        const formData = new URLSearchParams();
        formData.append("name", document.getElementById("name").value);
        formData.append("userId", document.querySelector("input[name='userId']").value);
        let url = "/save_group";
        let method = "POST";

        if (currentGroupId) {
            url = `/edit_group/${currentGroupId}`;
            method = "POST";
            formData.append("groupId", currentGroupId);
        }

        fetch(url, {
            method: method,
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: formData.toString()
        })
            .then(response => response.text())
            .then(() => location.reload())
            .catch(error => console.error("Error al guardar/actualizar grupo:", error));
    }

    function assignEvents() {
        btnNewGroup.addEventListener("click", openNewGroupModal);
        formNewGroup.addEventListener("submit", saveGroup);
        btnAddSelectedUser.addEventListener("click", handleChangeOwner);
        assignGroupButtonEvents();
    }

    assignEvents();
});
