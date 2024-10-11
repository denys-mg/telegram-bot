FROM rabbitmq:3.13.2-management-alpine
COPY ./definitions.json /etc/rabbitmq/
ENV RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_management load_definitions \"/etc/rabbitmq/definitions.json\""
