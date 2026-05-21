package com.muhdila.accurateuserapp.user.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.core.presentation.asString
import com.muhdila.accurateuserapp.user.domain.model.Gender
import com.muhdila.accurateuserapp.user.presentation.effect.UserEvent
import com.muhdila.accurateuserapp.user.presentation.state.UserFormState

@Composable
fun AddUserDialog(
    formState: UserFormState,
    isLoading: Boolean,
    onEvent: (UserEvent) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.add_user_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                    }
                }

                FormField(
                    value = formState.name,
                    onValueChange = { onEvent(UserEvent.NameChanged(it)) },
                    label = stringResource(R.string.full_name),
                    errorText = formState.nameError?.asString(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                FormField(
                    value = formState.address,
                    onValueChange = { onEvent(UserEvent.AddressChanged(it)) },
                    label = stringResource(R.string.address),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )
                FormField(
                    value = formState.email,
                    onValueChange = { onEvent(UserEvent.EmailChanged(it)) },
                    label = stringResource(R.string.email),
                    errorText = formState.emailError?.asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                FormField(
                    value = formState.phoneNumber,
                    onValueChange = { onEvent(UserEvent.PhoneChanged(it)) },
                    label = stringResource(R.string.phone_number),
                    errorText = formState.phoneError?.asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )
                FormField(
                    value = formState.city,
                    onValueChange = { onEvent(UserEvent.CityChanged(it)) },
                    label = stringResource(R.string.city),
                    errorText = formState.cityError?.asString(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    )
                )

                Text(
                    text = stringResource(R.string.gender),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Gender.entries.forEach { gender ->
                        FilterChip(
                            selected = formState.gender == gender,
                            onClick = { onEvent(UserEvent.GenderChanged(gender)) },
                            label = {
                                Text(if (gender == Gender.MALE) stringResource(R.string.gender_male_symbol) else stringResource(R.string.gender_female_symbol))
                            }
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = { onEvent(UserEvent.SubmitAddUser) },
                    enabled = formState.canSubmit && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.save), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorText != null,
        supportingText = errorText?.let { { Text(it) } },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
