// project.js
document.addEventListener("DOMContentLoaded", function() {
    const taskModal = document.getElementById("modalTask");
    const btnNewTask = document.getElementById("btnNewTask");
    const formNewTask = document.getElementById("formNewTask");
    const projectID = document.getElementById("project-info").dataset.projectid;
    let currentTaskId = null; // Para guardar el id de la tarea seleccionada

    // 🔹 Función para asignar eventos a los botones
    function asignarEventosBotones() {
        // Botones "Más opciones"
        document.querySelectorAll(".btnMoreOptions").forEach(button => {
            button.removeEventListener("click", handleMoreOptionsClick);
            button.addEventListener("click", handleMoreOptionsClick);
        });

        // Botones "Eliminar"
        document.querySelectorAll(".btnDeleteTask").forEach(button => {
            button.removeEventListener("click", handleDeleteTask);
            button.addEventListener("click", handleDeleteTask);
        });

        // Cerrar modal haciendo clic en el fondo
        document.querySelectorAll(".modal").forEach(modal => {
            modal.removeEventListener("click", handleModalClose);
            modal.addEventListener("click", handleModalClose);
        });
    }

    // 🔹 Manejador de "Más opciones" (abre el modal)
    function handleMoreOptionsClick(event) {
        currentTaskId = event.currentTarget.dataset.taskid;
        console.log("currentTaskId = " + currentTaskId);

        const taskItem = event.currentTarget.closest(".task-item");
        const modal = taskItem.querySelector(".modal");

        if (modal) {
            modal.style.display = "flex";

            // 🔹 Solo asignamos el evento si no está ya asignado
            if (!modal.dataset.eventAssigned) {
                modal.addEventListener("click", handleModalClose);
                modal.dataset.eventAssigned = "true"; // Marcar como asignado
            }
        }
    }

    // 🔹 Función de eliminación de tarea
    function handleDeleteTask(event) {
        const taskId = event.target.dataset.taskid;
        if (!taskId) {
            console.error("No se ha seleccionado una tarea para eliminar.");
            return;
        }
        console.log("Eliminando tarea con ID: " + taskId);

        // Cerrar el modal antes de eliminar
        const taskItem = event.target.closest(".task-item");
        const modal = taskItem.querySelector(".modal");
        if (modal) {
            modal.style.display = "none"; // Ocultar modal antes de eliminar la tarea
        }

        // 🔹 Buscar la tarea correcta
        if (!taskItem) {
            console.warn("No se encontró la tarea en el DOM.");
            return;
        }
        // 🔹 Aplicar animación de eliminación
        taskItem.style.transition = "opacity 0.3s ease-out";
        taskItem.style.opacity = "0";

        fetch(`/project/${projectID}/delete_task?taskId=${taskId}`, {
            method: "DELETE",
        })
            .then(response => {
                if (response.ok) {
                    console.log("Tarea eliminada correctamente");

                    setTimeout(() => {
                        taskItem.remove(); // Eliminar la tarea del DOM
                        asignarEventosBotones(); // Reasignar eventos para evitar problemas futuros
                    }, 300);
                } else {
                    console.error("Error al eliminar la tarea");
                }
            })
            .catch(error => console.error("Error en la petición:", error));
    }

    // 🔹 Cerrar el modal si se hace clic fuera del contenido
    function handleModalClose(event) {
        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
            currentTaskId = null;
        }
    }

    // 🔹 Mostrar el modal de "Nueva Tarea"
    btnNewTask.addEventListener("click", function() {
        taskModal.style.display = "flex";
    });

    // 🔹 Cerrar el modal de "Nueva Tarea" si se hace clic fuera
    window.addEventListener("click", function(event) {
        if (event.target === taskModal) {
            taskModal.style.display = "none";
        }
    });

    // 🔹 Enviar datos por AJAX y actualizar la lista de tareas
    formNewTask.addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = new FormData(formNewTask);
        formData.append("projectID", projectID);

        fetch(`/project/${projectID}/save_task`, {
            method: "POST",
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                console.log(data);
                // Puedes recargar la página o
                // despachar un evento para actualizar la lista sin refrescar:
                location.reload();
            })
            .catch(error => console.error("Error al guardar la tarea:", error));
    });

    // 🔹 Asignamos eventos al cargar la página
    asignarEventosBotones();
});
