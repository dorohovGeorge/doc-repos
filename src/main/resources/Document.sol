// SPDX_License_Identifier: MIT
pragma solidity ^0.8;

contract Document {
    mapping(string => string) documents;
    mapping(string => string) fio_document;
    mapping(string => string) docNumber_document;
    address contractOwner = msg.sender;

    function addDocument(string memory hash, string memory id, string memory fio, string memory docNum) public returns (uint256 timeStamp) {
        fio_document[fio] = id;
        docNumber_document[docNum] = id;
        documents[id] = hash;
        return block.timestamp;
    }

    function check(string memory fio, string memory docNum) public returns (string memory answer) {
        if (bytes(fio_document[fio]).length != 0 || bytes(docNumber_document[docNum]).length != 0) {
            return "true";
        } else {
            return "false";
        }
    }

    /* function verifyDocument(string memory hash) public returns(uint256 dateAdded){
         dateAdded = documents[hash];
         return dateAdded;
     }*/

    function getDocument(string memory id) public returns (string memory hash){
        hash = documents[id];
        return hash;
    }

    function getDocumentFromFio(string memory fio) public returns (string memory hash){
        if (bytes(fio_document[fio]).length != 0) {
            string memory id = fio_document[fio];
            hash = documents[id];
            return hash;
        } else {
            return "0";
        }

    }

    function getDocumentFromDocument(string memory docNumber) public returns (string memory hash){
        if (bytes(docNumber_document[docNumber]).length != 0) {
            string memory id = docNumber_document[docNumber];
            hash = documents[id];
            return hash;
        } else {
            return "0";
        }
    }
}
