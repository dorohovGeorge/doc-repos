import com.rockaport.alice.Alice
import com.rockaport.alice.AliceContextBuilder
import io.ipfs.api.IPFS
import io.ipfs.api.NamedStreamable
import io.ipfs.multihash.Multihash
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.passay.AllowedCharacterRule.ERROR_CODE
import org.passay.CharacterData
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.PasswordGenerator
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*


private val PRIVATE_KEY = "49dad87dd151b87df5402bb5426f66068f5ce0a41299a243a302fcc66f9a67c8"
private val ALCHEMY_API = "https://eth-sepolia.g.alchemy.com/v2/SJYBReW-r4_L-JR_3erSfrWsxesQi75Z"
private lateinit var web3j: Web3j
val ipfs = IPFS("localhost", 5001)
const val address_smart = "0xb7ea679acf2451c44236372588f366f2cfaf202a"
val alice = Alice(AliceContextBuilder().build())


enum class SearchingType {
    ID, FIO, DOCNUM
}

fun main() {
    embeddedServer(Netty, port = 9080, module = Application::server).start(wait = true)
}

private fun getDocumentFromSmartContract(web3j: Web3j, docAddress: String, value: String, type: SearchingType): String {
    var function: Function? = null
    when (type) {
        SearchingType.ID -> {
            function = Function(
                "getDocument", listOf(Utf8String(value)), listOf(object : TypeReference<Utf8String?>() {})
            )
        }

        SearchingType.FIO -> {
            function = Function(
                "getDocumentFromFio", listOf(Utf8String(value)), listOf(object : TypeReference<Utf8String?>() {})
            )
        }

        SearchingType.DOCNUM -> {
            function = Function(
                "getDocumentFromDocument", listOf(Utf8String(value)), listOf(object : TypeReference<Utf8String?>() {})
            )
        }
    }


    val encodedFunction = FunctionEncoder.encode(function)

    val response = web3j.ethCall(
        Transaction.createEthCallTransaction(
            "0x3D5599aAF4D4BAd40fc2F17159FD2c3CF87C8d23", docAddress, encodedFunction
        ), DefaultBlockParameterName.LATEST
    ).sendAsync().get()


    val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)

    val preValue = results[0] as Utf8String

    if(preValue.value == "0") {
        when (type) {
            SearchingType.FIO -> {
                return "Error: don't find this fio"
            }

            SearchingType.DOCNUM -> {
                return "Error: don't find this docnum"
            }

            else -> {}
        }
    }

    return preValue.value
}

private fun getCredentialsFromPrivateKey(): Credentials {
    return WalletUtils.loadCredentials(
        "12345",
        "/Users/george/Library/Ethereum/testnet/keystore/UTC--2023-04-12T00-13-43.17169000Z--3d5599aaf4d4bad40fc2f17159fd2c3cf87c8d23.json"
    )
}

fun generatePassayPassword(lenght: Int): String? {
    val gen = PasswordGenerator()
    val lowerCaseChars: CharacterData = EnglishCharacterData.LowerCase
    val lowerCaseRule = CharacterRule(lowerCaseChars)
    lowerCaseRule.numberOfCharacters = 2
    val upperCaseChars: CharacterData = EnglishCharacterData.UpperCase
    val upperCaseRule = CharacterRule(upperCaseChars)
    upperCaseRule.numberOfCharacters = 2
    val digitChars: CharacterData = EnglishCharacterData.Digit
    val digitRule = CharacterRule(digitChars)
    digitRule.numberOfCharacters = 2
    val specialChars: CharacterData = object : CharacterData {
        override fun getErrorCode(): String {
            return ERROR_CODE
        }

        override fun getCharacters(): String {
            return "!@#$%^&*()_+"
        }
    }
    val splCharRule = CharacterRule(specialChars)
    splCharRule.numberOfCharacters = 2
    return gen.generatePassword(
        lenght, splCharRule, lowerCaseRule, upperCaseRule, digitRule
    )
}

fun Application.server() {
    web3j = Web3j.build(HttpService(ALCHEMY_API))
    val document = Document.load(
        address_smart, web3j, getCredentialsFromPrivateKey(), DefaultGasProvider()
    )
    println(document.contractAddress)
    routing {
        get("/getDocument/{type}/{value}") {
            val type = call.parameters["type"]
            val value = call.parameters["value"]
            val searchingType = when (type) {
                "ID" -> SearchingType.ID
                "FIO" -> SearchingType.FIO
                "DOCNUM" -> SearchingType.DOCNUM
                else -> {
                    call.respondText("Wrong searching type")
                    return@get
                }
            }
            val ipfsId = getDocumentFromSmartContract(web3j, address_smart, value!!, searchingType)
            if(ipfsId.contains("Error")) {
                call.respondText(ipfsId)
            }
            val ipfsResult = ipfs.cat(Multihash.fromBase58(ipfsId))

            val password = "9)bdsYR3h&H^aVWgA1@3@4%2v".toCharArray()
            val decrypted = alice.decrypt(ipfsResult, password)

            //FileOutputStream("./test.pdf").use { fos -> fos.write(decrypted) }
            val outputFile = File("test.pdf")
            outputFile.writeBytes(decrypted)

            call.respondFile(outputFile)

//          call.respondText("Hello, ${getDocumentFromSmartContract(web3j, "0x212f26190b0333c052120e0526523992927b1f8c", "64c96be8-24e1-481c-b35f-b610295a9ef0")}!")
        }

//        get("/getDocumentByFio/{fio}") {
//            val fio = call.parameters["fio"]
//            val ipfsId = getDocumentFromSmartContract(web3j, address_smart, fio!!, SearchingType.FIO)
//            val ipfsResult = ipfs.cat(Multihash.fromBase58(ipfsId))
//
//            val password = "9)bdsYR3h&H^aVWgA1@3@4%2v".toCharArray()
//            val decrypted = alice.decrypt(ipfsResult, password)
//
//            //FileOutputStream("./test.pdf").use { fos -> fos.write(decrypted) }
//            val outputFile = File("test.pdf")
//            outputFile.writeBytes(decrypted)
//
//            call.respondFile(outputFile)
//        }

        get("/loadLocalDocument/{filename}") {
            val filename = call.parameters["filename"]
            val file = File("./$filename")

            val inputStream: InputStream = FileInputStream(file)
            val bytes = ByteArray(file.length().toInt())
            inputStream.read(bytes)

//    val password = generatePassayPassword(25)?.toCharArray()
            val password = "9)bdsYR3h&H^aVWgA1@3@4%2v".toCharArray()
            println(password.contentToString())

            val encrypted = alice.encrypt(bytes, password)

            ipfs.refs.local()

            val fileIPFS = NamedStreamable.ByteArrayWrapper(encrypted)
            val addResult = ipfs.add(fileIPFS)[0]

            call.respondText("Hi, id u document - ${addResult.hash}")
        }



        post("/loadDocument") {
            val multipart = call.receiveMultipart()
            var docNumber = ""
            var fullName = ""
            var dateBirth = ""
            var bytes: ByteArray? = null

            call.respondTextWriter {
                if (!call.request.isMultipart()) {
                    appendLine("Not a multipart request")
                } else {
                    while (true) {
                        val part = multipart.readPart() ?: break

                        when (part) {
                            is PartData.FormItem -> {
                                when (part.name) {
                                    "docNumber" -> {
                                        docNumber = part.value
                                        println("docNumber is $docNumber")
                                    }

                                    "fullName" -> {
                                        fullName = part.value
                                        println("FullName is $fullName")
                                    }

                                    "dateBirth" -> {
                                        dateBirth = part.value
                                        println("DateBirth is $dateBirth")
                                    }
                                }
                            }

                            is PartData.FileItem -> {
                                bytes = part.streamProvider().readBytes()
                                if (bytes == null || bytes!!.isEmpty() || docNumber.isEmpty() || fullName.isEmpty() || dateBirth.isEmpty()) {
                                    appendLine("U don't full all fields")
                                } else {
                                    var fio = fullName + dateBirth
                                    fio = fio.lowercase().trim()
                                    var function = Function(
                                        "getDocumentFromFio",
                                        listOf(Utf8String(fio)),
                                        listOf(object : TypeReference<Utf8String?>() {})
                                    )

                                    var encodedFunction = FunctionEncoder.encode(function)

                                    var response = web3j.ethCall(
                                        Transaction.createEthCallTransaction(
                                            "0x3D5599aAF4D4BAd40fc2F17159FD2c3CF87C8d23", address_smart, encodedFunction
                                        ), DefaultBlockParameterName.LATEST
                                    ).sendAsync().get()


                                    var results =
                                        FunctionReturnDecoder.decode(response.value, function.outputParameters)

                                    val fullNameValue = results[0] as Utf8String


                                    function = Function(
                                        "getDocumentFromDocument",
                                        listOf(Utf8String(docNumber)),
                                        listOf(object : TypeReference<Utf8String?>() {})
                                    )

                                    encodedFunction = FunctionEncoder.encode(function)

                                    response = web3j.ethCall(
                                        Transaction.createEthCallTransaction(
                                            "0x3D5599aAF4D4BAd40fc2F17159FD2c3CF87C8d23", address_smart, encodedFunction
                                        ), DefaultBlockParameterName.LATEST
                                    ).sendAsync().get()


                                    results = FunctionReturnDecoder.decode(response.value, function.outputParameters)

                                    val docValue = results[0] as Utf8String

                                    if (!fullNameValue.value.equals("0")) {
                                        appendLine("User with fullname already exist")
                                    } else if (!docValue.value.equals("0")) {
                                        appendLine("User with doc num already exist")
                                    } else {
                                        val password = "9)bdsYR3h&H^aVWgA1@3@4%2v".toCharArray()
                                        println(password.contentToString())

                                        val encrypted = alice.encrypt(bytes, password)

                                        ipfs.refs.local()

                                        val fileIPFS = NamedStreamable.ByteArrayWrapper(encrypted)

                                        val addResult = ipfs.add(fileIPFS)[0]
                                        println(addResult.hash)

                                        val id = UUID.randomUUID()

                                        println(fio)
                                        document.addDocument(
                                            addResult.hash.toString(), id.toString(), fio, docNumber
                                        ).send()

                                        appendLine("Hi, id u document - $id")
                                    }


                                }

                            }

                            else -> {}
                        }
                        part.dispose()
                    }
                }
            }

        }

        get("/") {
            call.respondHtml {
                body {
                    form(action = "/loadDocument", encType = FormEncType.multipartFormData, method = FormMethod.post) {
                        p {
                            +"FullName: "
                            textInput(name = "fullName")
                        }
                        p {
                            +"Date birth:"
                            dateInput(name = "dateBirth")
                        }
                        p {
                            +"Number of document"
                            textInput(name = "docNumber")
                        }
                        p {
                            +"File:"
                            fileInput(name = "file")
                        }
                        p {
                            submitInput { value = "Load document" }
                        }
                    }
                }
            }
        }

        get("/FIO") {
            call.respondHtml {
                body {
                    form(action = "/getDocument/FIO/{textInput}", encType = FormEncType.multipartFormData, method = FormMethod.post) {
                        p {
                            +"FullName: "
                            textInput(name = "fullName")
                        }
                        p {
                            +"Date birth:"
                            dateInput(name = "dateBirth")
                        }
                        p {
                            +"Number of document"
                            textInput(name = "docNumber")
                        }
                        p {
                            +"File:"
                            fileInput(name = "file")
                        }
                        p {
                            submitInput { value = "Load document" }
                        }
                    }
                }
            }
        }

    }
}
