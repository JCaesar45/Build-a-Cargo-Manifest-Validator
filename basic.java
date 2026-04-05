// Cargo Manifest Validator - Java Implementation

import java.util.HashMap;
import java.util.Map;

public class CargoManifestValidator {
    
    /**
     * Represents a cargo manifest
     */
    public static class Manifest {
        public Integer containerId;
        public String destination;
        public Double weight;
        public String unit;
        public Boolean hazmat;
        
        public Manifest() {}
        
        public Manifest(Integer containerId, String destination, Double weight, String unit, Boolean hazmat) {
            this.containerId = containerId;
            this.destination = destination;
            this.weight = weight;
            this.unit = unit;
            this.hazmat = hazmat;
        }
    }
    
    /**
     * Normalizes the weight to kilograms without mutating the original manifest
     * @param manifest The cargo manifest object
     * @return A new manifest object with weight in kg
     */
    public static Map<String, Object> normalizeUnits(Map<String, Object> manifest) {
        // Create a copy of the manifest to avoid mutation
        Map<String, Object> normalized = new HashMap<>(manifest);
        
        // Convert pounds to kilograms if necessary
        if ("lb".equals(normalized.get("unit"))) {
            Double weight = (Double) normalized.get("weight");
            normalized.put("weight", weight * 0.45);
            normalized.put("unit", "kg");
        }
        
        return normalized;
    }
    
    /**
     * Validates a cargo manifest and returns validation errors
     * @param manifest The cargo manifest object
     * @return A map containing missing/invalid properties, or empty if valid
     */
    public static Map<String, String> validateManifest(Map<String, Object> manifest) {
        Map<String, String> errors = new HashMap<>();
        
        // Check containerId: must be a positive integer
        if (!manifest.containsKey("containerId")) {
            errors.put("containerId", "Missing");
        } else {
            Object containerId = manifest.get("containerId");
            if (!(containerId instanceof Integer) || (Integer) containerId <= 0) {
                errors.put("containerId", "Invalid");
            }
        }
        
        // Check destination: must be a non-empty string after trimming
        if (!manifest.containsKey("destination")) {
            errors.put("destination", "Missing");
        } else {
            Object destination = manifest.get("destination");
            if (!(destination instanceof String) || ((String) destination).trim().isEmpty()) {
                errors.put("destination", "Invalid");
            }
        }
        
        // Check weight: must be a positive number (not NaN)
        if (!manifest.containsKey("weight")) {
            errors.put("weight", "Missing");
        } else {
            Object weight = manifest.get("weight");
            if (!(weight instanceof Number) || 
                Double.isNaN(((Number) weight).doubleValue()) ||
                ((Number) weight).doubleValue() <= 0) {
                errors.put("weight", "Invalid");
            }
        }
        
        // Check unit: must be "kg" or "lb"
        if (!manifest.containsKey("unit")) {
            errors.put("unit", "Missing");
        } else {
            Object unit = manifest.get("unit");
            if (!"kg".equals(unit) && !"lb".equals(unit)) {
                errors.put("unit", "Invalid");
            }
        }
        
        // Check hazmat: must be a boolean
        if (!manifest.containsKey("hazmat")) {
            errors.put("hazmat", "Missing");
        } else {
            Object hazmat = manifest.get("hazmat");
            if (!(hazmat instanceof Boolean)) {
                errors.put("hazmat", "Invalid");
            }
        }
        
        return errors;
    }
    
    /**
     * Processes a cargo manifest, logging validation results
     * @param manifest The cargo manifest object
     */
    public static void processManifest(Map<String, Object> manifest) {
        Map<String, String> errors = validateManifest(manifest);
        boolean isValid = errors.isEmpty();
        
        if (isValid) {
            System.out.println("Validation success: " + manifest.get("containerId"));
            Map<String, Object> normalized = normalizeUnits(manifest);
            System.out.println("Total weight: " + normalized.get("weight") + " kg");
        } else {
            System.out.println("Validation error: " + manifest.get("containerId"));
            System.out.println(errors);
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        // Test normalizeUnits
        Map<String, Object> testManifest = new HashMap<>();
        testManifest.put("containerId", 68);
        testManifest.put("destination", "Salinas");
        testManifest.put("weight", 101.0);
        testManifest.put("unit", "lb");
        testManifest.put("hazmat", true);
        
        System.out.println("Original: " + testManifest);
        Map<String, Object> normalized = normalizeUnits(testManifest);
        System.out.println("Normalized: " + normalized);
        System.out.println("Original after normalization (should be unchanged): " + testManifest);
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Test validateManifest with valid manifest
        Map<String, Object> validManifest = new HashMap<>();
        validManifest.put("containerId", 1);
        validManifest.put("destination", "Santa Cruz");
        validManifest.put("weight", 304.0);
        validManifest.put("unit", "kg");
        validManifest.put("hazmat", false);
        System.out.println("Validating valid manifest: " + validateManifest(validManifest));
        
        // Test validateManifest with empty map
        System.out.println("Validating empty manifest: " + validateManifest(new HashMap<>()));
        
        // Test validateManifest with invalid containerId
        Map<String, Object> invalidContainer = new HashMap<>();
        invalidContainer.put("containerId", null);
        invalidContainer.put("destination", "Santa Cruz");
        invalidContainer.put("weight", 304.0);
        invalidContainer.put("unit", "kg");
        invalidContainer.put("hazmat", false);
        System.out.println("Validating invalid containerId: " + validateManifest(invalidContainer));
        
        // Test processManifest
        System.out.println("\n" + "=".repeat(60) + "\n");
        System.out.println("Processing valid manifest:");
        Map<String, Object> processTest = new HashMap<>();
        processTest.put("containerId", 55);
        processTest.put("destination", "Carmel");
        processTest.put("weight", 400.0);
        processTest.put("unit", "lb");
        processTest.put("hazmat", false);
        processManifest(processTest);
        
        System.out.println("\nProcessing invalid manifest:");
        Map<String, Object> invalidProcess = new HashMap<>();
        invalidProcess.put("containerId", -88);
        invalidProcess.put("destination", "Soledad");
        invalidProcess.put("weight", Double.NaN);
        processManifest(invalidProcess);
    }
}
