package com.example.demo.api;

import com.example.demo.service.impl.ClientService;
import com.example.demo.service.model.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
public class ClientControllerGrpc extends clientServiceGrpc.clientServiceImplBase{
    private final ClientService clientService;

    @Autowired
    public ClientControllerGrpc(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void addClient(addClientRequest request, StreamObserver<addClientResponse> responseStreamObserver) {
        clientService.addClient(new Client(request.getClient().getName(), request.getClient().getPresent(),
                request.getClient().getVip()));
        addClientResponse response = addClientResponse.newBuilder().build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void deleteClient(deleteClientRequest request, StreamObserver<deleteClientResponse> responseStreamObserver){
        clientService.deleteClient(UUID.fromString(request.getId()));
        deleteClientResponse response = deleteClientResponse.newBuilder().build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getAllClients(getClientsRequest request, StreamObserver<getClientsResponse> responseObserver){
        List<Client> clients = clientService.getClientRepository().findAll();

        List<getClientResponse> clientResponses = new ArrayList<>();
        for (Client client : clients) {
            getClientResponse ClientResponse= getClientResponse.newBuilder()
                    .setId(client.getId().toString())
                    .setName(client.getName())
                    .setPresent(false)
                    .setVip(false)
                    .build();
            clientResponses.add(ClientResponse);
        }
        getClientsResponse response = getClientsResponse.newBuilder().addAllClients(clientResponses).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}