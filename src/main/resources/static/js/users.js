document.addEventListener("DOMContentLoaded", function () {
    const btnNewMember = document.getElementById("btnNewMember");
    let currentUserId = null;
    let clickInsideModal = false;

    function assignUserButtonEvents() {
        document.querySelectorAll(".btnMoreOptions").forEach(button => {
            button.removeEventListener("click", handleMoreOptionsClick);
            button.addEventListener("click", handleMoreOptionsClick);
        });

        document.querySelectorAll(".btnDeleteMember").forEach(button => {
            button.removeEventListener("click", handleDeleteMember);
            button.addEventListener("click", handleDeleteMember);
        });

        document.querySelectorAll(".modal").forEach(modal => {
            modal.removeEventListener("mousedown", handleModalMouseDown);
            modal.removeEventListener("mouseup", handleModalMouseUp);
            modal.addEventListener("mousedown", handleModalMouseDown);
            modal.addEventListener("mouseup", handleModalMouseUp);
        });
    }

    function handleMoreOptionsClick(event) {
        currentUserId = event.currentTarget.dataset.userid;
        const userItem = event.currentTarget.closest(".user-item");
        const modal = userItem.querySelector(".modalOptions");

        if (modal) {
            modal.classList.remove("hidden");
            modal.style.display = "flex";
        }
    }

    function handleDeleteMember(event) {
        const userId = event.target.dataset.userid;
        const groupId = document.body.dataset.groupid;
        if (!userId || !groupId) {
            console.error("No se ha seleccionado un usuario o grupo para eliminar.");
            return;
        }

        if (confirm("¿Estás seguro de que deseas eliminar este miembro del grupo?")) {
            fetch(`/delete_member/${userId}?groupId=${groupId}`, {
                method: "POST",
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        location.reload();
                    } else {
                        alert("Error al eliminar el miembro");
                    }
                })
                .catch(error => console.error("Error en la petición:", error));
        }
    }

    function handleModalMouseDown(event) {
        clickInsideModal = !!event.target.closest(".modal-content");
    }

    function handleModalMouseUp(event) {
        if (!clickInsideModal && event.target.classList.contains("modal")) {
            event.target.classList.add("hidden");
            event.target.style.display = "none";
        }
    }

    function assignEvents() {
        btnNewMember.addEventListener("click", function () {
            alert("Funcionalidad para agregar nuevo miembro aún no implementada.");
        });
        assignUserButtonEvents();
    }

    assignEvents();
});
