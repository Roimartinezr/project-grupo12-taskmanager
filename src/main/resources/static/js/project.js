document.addEventListener("DOMContentLoaded", function() {
    const taskModal = document.getElementById("modalTask");
    const btnNewTask = document.getElementById("btnNewTask");
    const formNewTask = document.getElementById("formNewTask");
    const projectID = document.getElementById("project-info").dataset.projectid;
    const modalOptions = document.getElementById("modalOptions");
    let currentTaskId = null; // Para guardar el id de la tarea seleccionada
    const btnDeleteTask = document.getElementById("btnDeleteTask");
    const btnEditTask = document.getElementById("btnEditTask");

    // 🔹 Función para asignar eventos a los botones "Más opciones"
    function asignarEventosBotones() {
        document.querySelectorAll(".btnMoreOptions").forEach(button => {
            button.removeEventListener("click", handleMoreOptionsClick);
            button.addEventListener("click", handleMoreOptionsClick);
        });
    }

    // 🔹 Manejador del evento "Más opciones"
    function handleMoreOptionsClick(event) {
        currentTaskId = event.currentTarget.dataset.taskid; // revisar que ID se esta guardando 

        // Obtener el modal de la tarea específica
        const taskItem = event.currentTarget.closest(".task-item");
        const modal = taskItem.querySelector(".modal");

        if (modal) {
            modal.style.display = "flex"; // Mostrar el modal específico de la tarea

            // 🔹 Asegurar que el modal se cierre correctamente
            modal.removeEventListener("click", handleModalClose);
            modal.addEventListener("click", handleModalClose);
        }

        // 🔹 Actualizar el título de la tarea en el modal
        const taskTitle = taskItem.querySelector("b").innerText;
        modal.querySelector("h2").innerText = taskTitle;
    }

    // 🔹 Función para cerrar el modal si se hace clic fuera
    function handleModalClose(event) {
        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
            currentTaskId = null;
        }
    }

    // 🔹 Asignamos eventos inicialmente
    asignarEventosBotones();

    // 🔹 Mostrar el modal cuando se hace clic en el botón
    btnNewTask.addEventListener("click", function() {
        taskModal.style.display = "flex";
    });

    // 🔹 Cerrar el modal de "Nueva Tarea" si se hace clic fuera de él
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
                location.reload();
            })
            .catch(error => console.error("Error al guardar la tarea:", error));
    });

    // 🔹 Manejo de eliminación de tarea
    btnDeleteTask.addEventListener("click", function() {
        if (!currentTaskId) {
            console.error("No se ha seleccionado una tarea para eliminar.");
            return;
        }

        fetch(`/project/${projectID}/delete_task?taskId=${currentTaskId}`, {
            method: "DELETE",
        })
            .then(response => {
                if (response.ok) {
                    console.log("Tarea eliminada correctamente");

                    // 🔹 Encontrar la tarea y su modal
                    const taskElement = document.querySelector(`.task-item button[data-taskid='${currentTaskId}']`).closest(".task-item");
                    const modal = taskElement.querySelector(".modal");

                    if (modal) {
                        modal.style.display = "none"; // Cerrar modal específico
                    }

                    if (taskElement) {
                        taskElement.style.transition = "opacity 0.3s ease-out";
                        taskElement.style.opacity = "0"; // Animación de desaparición

                        setTimeout(() => {
                            taskElement.remove(); // Eliminar el elemento del DOM
                            currentTaskId = null;
                            asignarEventosBotones(); // 🔹 Volver a asignar eventos
                        }, 300);
                    } else {
                        console.warn("No se encontró la tarea en el DOM.");
                    }
                } else {
                    console.error("Error al eliminar la tarea");
                }
            })
            .catch(error => console.error("Error en la petición:", error));
    });

    btnEditTask.addEventListener("click", function() {
        console.log("Editar tarea con id:", currentTaskId);
    });
});
