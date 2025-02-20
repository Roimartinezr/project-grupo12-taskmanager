document.addEventListener("DOMContentLoaded", function() {
    const taskModal = document.getElementById("modalTask");
    const btnNewTask = document.getElementById("btnNewTask");
    const formNewTask = document.getElementById("formNewTask");
    const projectID = document.getElementById("project-info").dataset.projectid;
    const modalOptions = document.getElementById("modalOptions");
    let currentTaskId = null; // Para guardar el id de la tarea seleccionada
    const btnDeleteTask = document.getElementById("btnDeleteTask");
    const btnEditTask = document.getElementById("btnEditTask");

    // Mostrar el modal cuando se hace clic en el botón
    btnNewTask.addEventListener("click", function() {
        taskModal.style.display = "flex";
    });

    // Cerrar el modal si se hace clic fuera de él
    window.addEventListener("click", function(event) {
        if (event.target === taskModal) {
            taskModal.style.display = "none";
        }
    });

    // Enviar datos por AJAX y actualizar la lista de tareas
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

    // Manejo de click en los botones "Más opciones"
    document.querySelectorAll(".btnMoreOptions").forEach(button => {
        button.addEventListener("click", function() {
            currentTaskId = this.dataset.taskid; // Guardamos el id de la tarea
            modalOptions.style.display = "flex";
        });
    });

    // Cerrar al hacer clic fuera
    window.addEventListener("click", function(event) {
        if (event.target === modalOptions) {
            modalOptions.style.display = "none";
            currentTaskId = null;
        }
    });

    btnDeleteTask.addEventListener("click", function() {
        // Llamar a un endpoint que elimine la tarea currentTaskId
        console.log("Eliminar tarea con id:", currentTaskId);
        // Aquí harías una petición tipo:
        // fetch(`/tasks/${currentTaskId}/delete`, { method: "DELETE" }) ...
    });

    btnEditTask.addEventListener("click", function() {
        // Llamar a un endpoint o mostrar otro modal para editar la tarea
        console.log("Editar tarea con id:", currentTaskId);
        // Podrías reutilizar el modal de "Nueva Tarea" o crear uno nuevo
        // para la edición. Aqu todo depende de tu implementación final.
    });

    // actualizar el titulo de tarea que sale en la ventana opciones
    document.querySelectorAll(".btnMoreOptions").forEach(button => {
        button.addEventListener("click", function() {
            currentTaskId = this.dataset.taskid; // Guardamos el id de la tarea

            // Obtener el título de la tarea actual
            const taskTitle = this.closest(".task-item").querySelector("b").innerText;

            // Actualizar el título dentro del modal de opciones
            modalOptions.querySelector("h2").innerText = taskTitle;

            modalOptions.style.display = "flex";
        });
    });

});
