package com.thomascook.dreamcatcher.application

/**
 * Collection of application specific exceptions
 */

// Thrown to specify that a realm transaction failed
class RepositoryException(message: String) : Exception(message)