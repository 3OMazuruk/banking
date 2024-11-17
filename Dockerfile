# ==== CONFIGURE =====
# Use a JDK 11 as base image
FROM openjdk:11

# Copy source code
ADD . /code/

# Create a log directory for server
RUN mkdir /var/log/server-logs

# Update permission for read/write
RUN chmod 777 /var/log/server-logs

# Create the working directory inside the container
RUN mkdir /application

# Build jar with prod profile
RUN echo '{ "allow_root": true }' > /root/.bowerrc && \
    rm -Rf /code/target /code/node_modules && \
    cd /code/ && \
    ./mvnw clean package -Pprod -DskipTests && \
    mv ./target/*.jar /application/app.jar

# Remove source code
RUN rm -r /code

# Set env variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    SLEEP_BEFORE_START=5 \
    JAVA_OPTS="-Xms1024m -Xmx2048m"

# Command for run jar file during launch the container
# ==== RUN =======
CMD echo "The application will start in ${SLEEP_BEFORE_START}s..." && \
    sleep ${SLEEP_BEFORE_START} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /application/app.jar
