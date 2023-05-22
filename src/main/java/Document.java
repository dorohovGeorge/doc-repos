import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class Document extends Contract {
    public static final String BINARY = "6080604052600380546001600160a01b0319163317905534801561002257600080fd5b50610898806100326000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80630e72ca7a1461005c5780635dbdab3014610082578063725dbc40146100a25780637ccb6a64146100b5578063e44c36ae146100c8575b600080fd5b61006f61006a366004610558565b6100db565b6040519081526020015b60405180910390f35b610095610090366004610605565b61016b565b6040516100799190610666565b6100956100b0366004610699565b610314565b6100956100c3366004610605565b6103c1565b6100956100d6366004610605565b610471565b6000836001846040516100ee91906106fd565b9081526020016040518091039020908161010891906107a2565b508360028360405161011a91906106fd565b9081526020016040518091039020908161013491906107a2565b508460008560405161014691906106fd565b9081526020016040518091039020908161016091906107a2565b504295945050505050565b606060018260405161017d91906106fd565b9081526020016040518091039020805461019690610719565b1590506102f75760006001836040516101af91906106fd565b908152602001604051809103902080546101c890610719565b80601f01602080910402602001604051908101604052809291908181526020018280546101f490610719565b80156102415780601f1061021657610100808354040283529160200191610241565b820191906000526020600020905b81548152906001019060200180831161022457829003601f168201915b5050505050905060008160405161025891906106fd565b9081526020016040518091039020805461027190610719565b80601f016020809104026020016040519081016040528092919081815260200182805461029d90610719565b80156102ea5780601f106102bf576101008083540402835291602001916102ea565b820191906000526020600020905b8154815290600101906020018083116102cd57829003601f168201915b5050505050915050919050565b50506040805180820190915260018152600360fc1b602082015290565b606060018360405161032691906106fd565b9081526020016040518091039020805461033f90610719565b1515905080610377575060028260405161035991906106fd565b9081526020016040518091039020805461037290610719565b151590505b1561039d57506040805180820190915260048152637472756560e01b60208201526103bb565b5060408051808201909152600581526466616c736560d81b60208201525b92915050565b60606000826040516103d391906106fd565b908152602001604051809103902080546103ec90610719565b80601f016020809104026020016040519081016040528092919081815260200182805461041890610719565b80156104655780601f1061043a57610100808354040283529160200191610465565b820191906000526020600020905b81548152906001019060200180831161044857829003601f168201915b50505050509050919050565b606060028260405161048391906106fd565b9081526020016040518091039020805461049c90610719565b1590506102f75760006002836040516101af91906106fd565b634e487b7160e01b600052604160045260246000fd5b600082601f8301126104dc57600080fd5b813567ffffffffffffffff808211156104f7576104f76104b5565b604051601f8301601f19908116603f0116810190828211818310171561051f5761051f6104b5565b8160405283815286602085880101111561053857600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806000806080858703121561056e57600080fd5b843567ffffffffffffffff8082111561058657600080fd5b610592888389016104cb565b955060208701359150808211156105a857600080fd5b6105b4888389016104cb565b945060408701359150808211156105ca57600080fd5b6105d6888389016104cb565b935060608701359150808211156105ec57600080fd5b506105f9878288016104cb565b91505092959194509250565b60006020828403121561061757600080fd5b813567ffffffffffffffff81111561062e57600080fd5b61063a848285016104cb565b949350505050565b60005b8381101561065d578181015183820152602001610645565b50506000910152565b6020815260008251806020840152610685816040850160208701610642565b601f01601f19169190910160400192915050565b600080604083850312156106ac57600080fd5b823567ffffffffffffffff808211156106c457600080fd5b6106d0868387016104cb565b935060208501359150808211156106e657600080fd5b506106f3858286016104cb565b9150509250929050565b6000825161070f818460208701610642565b9190910192915050565b600181811c9082168061072d57607f821691505b60208210810361074d57634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561079d57600081815260208120601f850160051c8101602086101561077a5750805b601f850160051c820191505b8181101561079957828155600101610786565b5050505b505050565b815167ffffffffffffffff8111156107bc576107bc6104b5565b6107d0816107ca8454610719565b84610753565b602080601f83116001811461080557600084156107ed5750858301515b600019600386901b1c1916600185901b178555610799565b600085815260208120601f198616915b8281101561083457888601518255948401946001909101908401610815565b50858210156108525787850151600019600388901b60f8161c191681555b5050505050600190811b0190555056fea2646970667358221220116b68c157bbf2e89a69c701d92d1bf573773d67b3a94b03eeedfb8166f8cf5264736f6c63430008130033";

    public static final String FUNC_ADDDOCUMENT = "addDocument";

    public static final String FUNC_CHECK = "check";

    public static final String FUNC_GETDOCUMENT = "getDocument";

    public static final String FUNC_GETDOCUMENTFROMDOCUMENT = "getDocumentFromDocument";

    public static final String FUNC_GETDOCUMENTFROMFIO = "getDocumentFromFio";

    @Deprecated
    protected Document(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Document(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Document(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Document(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addDocument(String hash, String id, String fio, String docNum) {
        final Function function = new Function(
                FUNC_ADDDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(hash), 
                new org.web3j.abi.datatypes.Utf8String(id), 
                new org.web3j.abi.datatypes.Utf8String(fio), 
                new org.web3j.abi.datatypes.Utf8String(docNum)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> check(String fio, String docNum) {
        final Function function = new Function(
                FUNC_CHECK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fio), 
                new org.web3j.abi.datatypes.Utf8String(docNum)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getDocument(String id) {
        final Function function = new Function(
                FUNC_GETDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getDocumentFromDocument(String docNumber) {
        final Function function = new Function(
                FUNC_GETDOCUMENTFROMDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(docNumber)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getDocumentFromFio(String fio) {
        final Function function = new Function(
                FUNC_GETDOCUMENTFROMFIO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fio)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Document load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Document(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Document load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Document(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Document load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Document(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Document load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Document(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Document> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Document.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Document> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Document.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Document> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Document.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Document> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Document.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
