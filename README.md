# PatientBatchLoader

This application was originally generated using JHipster 4.13.0, you can find documentation and help at [http://www.jhipster.tech/documentation-archive/v4.13.0](http://www.jhipster.tech/documentation-archive/v4.13.0). After generation, I stripped out many of the features that were unnecessary for the Spring Batch demo. 

## Development

To start your application in the dev profile, simply run:

    ./gradlew

## Testing

To launch your application's tests, run:

    ./gradlew test

## Building for production

To optimize the PatientBatchLoader application for production, run:

    ./gradlew -Pprod clean bootRepackage

To ensure everything worked, run:

    java -jar build/libs/*.war
