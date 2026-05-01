package com.daqem.coldcase.database.service;

public interface Services {

    BlockService BLOCK = new BlockService(com.daqem.coldcase.ColdCase.getDatabase());
    ColdCaseBlockService COLD_CASE_BLOCK = new ColdCaseBlockService(BLOCK);
    ChatService CHAT = new ChatService(com.daqem.coldcase.ColdCase.getDatabase());
    CommandService COMMAND = new CommandService(com.daqem.coldcase.ColdCase.getDatabase());
    ContainerService CONTAINER = new ContainerService(com.daqem.coldcase.ColdCase.getDatabase());
    EntityService ENTITY = new EntityService(com.daqem.coldcase.ColdCase.getDatabase());
    ItemService ITEM = new ItemService(com.daqem.coldcase.ColdCase.getDatabase());
    LevelService LEVEL = new LevelService(com.daqem.coldcase.ColdCase.getDatabase());
    MaterialService MATERIAL = new MaterialService(com.daqem.coldcase.ColdCase.getDatabase());
    SessionService SESSION = new SessionService(com.daqem.coldcase.ColdCase.getDatabase());
    UsernameService USERNAME = new UsernameService(com.daqem.coldcase.ColdCase.getDatabase());
    UserService USER = new UserService(com.daqem.coldcase.ColdCase.getDatabase());
}
