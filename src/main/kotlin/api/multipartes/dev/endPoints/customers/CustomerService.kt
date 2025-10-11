package api.multipartes.dev.endPoints.customers

import api.multipartes.dev.dtos.CreateCustomerRequest
import api.multipartes.dev.dtos.CustomerResponse
import api.multipartes.dev.dtos.UpdateCustomerRequest
import api.multipartes.dev.models.Customer
import api.multipartes.dev.models.CustomerInvoiceData
import api.multipartes.dev.repositories.CustomerInvoiceDataRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val invoiceDataRepository: CustomerInvoiceDataRepository
) {

    @Cacheable("customers")
    fun getAllCustomers(): List<CustomerResponse> {
        return customerRepository.findAll().map { customer ->
            mapToResponse(customer)
        }
    }

    fun getCustomerById(id: Int): CustomerResponse {
        val customer = customerRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Customer with ID $id not found") }
        return mapToResponse(customer)
    }

    fun getCustomerByPhone(phone: String): CustomerResponse {
        val customer = customerRepository.findByPhone(phone)
            ?: throw IllegalArgumentException("Customer with phone $phone not found")
        return mapToResponse(customer)
    }

    fun getCustomerByRfc(rfc: String): CustomerResponse {
        val customer = customerRepository.findByInvoiceData_Rfc(rfc)
            ?: throw IllegalArgumentException("Customer with RFC $rfc not found")
        return mapToResponse(customer)
    }

    fun searchCustomersByName(name: String): List<CustomerResponse> {
        return customerRepository.findByNameContainingIgnoreCase(name).map { customer ->
            mapToResponse(customer)
        }
    }

    @CacheEvict(value = ["customers"], allEntries = true)
    fun createCustomer(request: CreateCustomerRequest): CustomerResponse {
        if (customerRepository.existsByPhone(request.phone)) {
            throw IllegalArgumentException("Customer with phone '${request.phone}' already exists")
        }

        var invoiceData: CustomerInvoiceData? = null
        if (request.rfc != null) {
            invoiceData = invoiceDataRepository.findById(request.rfc).orElse(null)
            if (invoiceData == null) {
                throw IllegalArgumentException("Invoice data with RFC '${request.rfc}' not found")
            }
        }

        val newCustomer = Customer(
            name = request.name,
            phone = request.phone,
            invoiceData = invoiceData
        )

        val savedCustomer = customerRepository.save(newCustomer)
        return mapToResponse(savedCustomer)
    }

    @CacheEvict(value = ["customers"], allEntries = true)
    fun updateCustomer(id: Int, request: UpdateCustomerRequest): CustomerResponse {
        val existingCustomer = customerRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Customer with ID $id not found") }

        if (customerRepository.existsByPhone(request.phone) && existingCustomer.phone != request.phone) {
            throw IllegalArgumentException("Customer with phone '${request.phone}' already exists")
        }

        var invoiceData: CustomerInvoiceData? = null
        if (request.rfc != null) {
            invoiceData = invoiceDataRepository.findById(request.rfc).orElse(null)
            if (invoiceData == null) {
                throw IllegalArgumentException("Invoice data with RFC '${request.rfc}' not found")
            }
        }

        val updatedCustomer = existingCustomer.copy(
            name = request.name,
            phone = request.phone,
            invoiceData = invoiceData
        )

        val savedCustomer = customerRepository.save(updatedCustomer)
        return mapToResponse(savedCustomer)
    }

    @CacheEvict(value = ["customers", "customer-issues"], allEntries = true)
    fun deleteCustomer(id: Int) {
        if (!customerRepository.existsById(id)) {
            throw IllegalArgumentException("Customer with ID $id not found")
        }
        customerRepository.deleteById(id)
    }

    private fun mapToResponse(customer: Customer): CustomerResponse {
        return CustomerResponse(
            id = customer.id!!,
            name = customer.name,
            phone = customer.phone,
            rfc = customer.invoiceData?.rfc
        )
    }
}
