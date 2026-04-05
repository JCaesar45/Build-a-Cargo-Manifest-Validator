// Cargo Manifest Validator - JavaScript Implementation

/**
 * Normalizes the weight to kilograms without mutating the original manifest
 * @param {Object} manifest - The cargo manifest object
 * @returns {Object} A new manifest object with weight in kg
 */
function normalizeUnits(manifest) {
  // Create a shallow copy of the manifest to avoid mutation
  const normalized = { ...manifest };
  
  // Convert pounds to kilograms if necessary
  if (normalized.unit === "lb") {
    normalized.weight = normalized.weight * 0.45;
    normalized.unit = "kg";
  }
  
  return normalized;
}

/**
 * Validates a cargo manifest and returns validation errors
 * @param {Object} manifest - The cargo manifest object
 * @returns {Object} An object containing missing/invalid properties, or empty if valid
 */
function validateManifest(manifest) {
  const errors = {};
  
  // Check containerId: must be a positive integer
  if (!manifest.hasOwnProperty('containerId')) {
    errors.containerId = "Missing";
  } else if (!Number.isInteger(manifest.containerId) || manifest.containerId <= 0) {
    errors.containerId = "Invalid";
  }
  
  // Check destination: must be a non-empty string after trimming
  if (!manifest.hasOwnProperty('destination')) {
    errors.destination = "Missing";
  } else if (typeof manifest.destination !== 'string' || manifest.destination.trim() === '') {
    errors.destination = "Invalid";
  }
  
  // Check weight: must be a positive number (not NaN)
  if (!manifest.hasOwnProperty('weight')) {
    errors.weight = "Missing";
  } else if (typeof manifest.weight !== 'number' || Number.isNaN(manifest.weight) || manifest.weight <= 0) {
    errors.weight = "Invalid";
  }
  
  // Check unit: must be "kg" or "lb"
  if (!manifest.hasOwnProperty('unit')) {
    errors.unit = "Missing";
  } else if (manifest.unit !== "kg" && manifest.unit !== "lb") {
    errors.unit = "Invalid";
  }
  
  // Check hazmat: must be a boolean
  if (!manifest.hasOwnProperty('hazmat')) {
    errors.hazmat = "Missing";
  } else if (typeof manifest.hazmat !== 'boolean') {
    errors.hazmat = "Invalid";
  }
  
  return errors;
}

/**
 * Processes a cargo manifest, logging validation results
 * @param {Object} manifest - The cargo manifest object
 */
function processManifest(manifest) {
  const errors = validateManifest(manifest);
  const isValid = Object.keys(errors).length === 0;
  
  if (isValid) {
    console.log(`Validation success: ${manifest.containerId}`);
    const normalized = normalizeUnits(manifest);
    console.log(`Total weight: ${normalized.weight} kg`);
  } else {
    console.log(`Validation error: ${manifest.containerId}`);
    console.log(errors);
  }
}

// Export for testing
if (typeof module !== 'undefined' && module.exports) {
  module.exports = { normalizeUnits, validateManifest, processManifest };
}
