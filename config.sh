#!/bin/bash

echo "Current directory: $(pwd)"

# Set up local Git configuration

echo "Starting the Git configuration setup..."

# Set the local Git configuration to include the parent .gitconfig file
echo "Setting up local Git configuration to include ../.githooks/"

git config --local core.hooksPath .githooks/

# Check if the command was successful
if [ $? -eq 0 ]; then
    echo "Git hooks configuration setup successful!"
else
    echo "Failed to set up Git hooks configuration. Please check the path and try again."
fi

# Print a message to indicate the end of the script
echo "Project configuration setup completed."